package com.kau.ttokttok.ui.xml.calendar

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kau.ttokttok.databinding.FragmentNoiseMeasurementBinding
import com.kau.ttokttok.domain.repository.RecordingRepository
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import kotlin.coroutines.coroutineContext
import kotlin.math.log10
import kotlin.math.sqrt
import javax.inject.Inject

@AndroidEntryPoint
class NoiseMeasurementFragment : Fragment() {
    private var _binding: FragmentNoiseMeasurementBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var recordingRepository: RecordingRepository

    private var audioRecord: AudioRecord? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var recordingFilePath: String? = null
    private var isRecording = false
    private var isPlaying = false
    private var recordingJob: Job? = null
    private var timerJob: Job? = null
    private var maxDb = 0.0
    private var avgDb = 0.0
    private val dbList = mutableListOf<Double>()
    private var startTime = 0L
    private var baselineRms: Double? = null // 캘리브레이션 기준점
    private var calibrationCount = 0
    private val calibrationRmsList = mutableListOf<Double>()
    private var smoothedDb: Double? = null // 스무딩된 데시벨
    private var rmsEma: Double? = null // RMS 지수 이동 평균

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startMeasurement()
        else Toast.makeText(requireContext(), "마이크 권한이 필요합니다", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNoiseMeasurementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            if (isRecording) stopMeasurement()
            findNavController().popBackStack()
        }
        binding.btnControl.setOnClickListener {
            if (isRecording) stopMeasurementAndShowButtons() else checkPermissionAndStart()
        }
        binding.btnPlayRecording.setOnClickListener {
            togglePlayRecording()
        }
        binding.btnComplete.setOnClickListener {
            navigateToNoiseLogForm()
        }
        updateControlButton(false)
    }

    private fun updateControlButton(recording: Boolean) {
        binding.btnControl.setImageResource(
            if (recording) android.R.drawable.ic_media_pause else android.R.drawable.ic_btn_speak_now
        )
        binding.tvSubtitle.text = if (recording) "측정을 완료하려면 버튼을 누르세요" else "측정 버튼을 눌러 시작하세요"
        binding.statsContainer.visibility = if (recording) View.VISIBLE else View.INVISIBLE

        if (!recording) {
            binding.circularGauge.reset()
            binding.tvCurrentDb.text = "--"
        }
    }

    private fun checkPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startMeasurement()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(requireContext(), "소음을 측정하기 위해 마이크 권한이 필요합니다", Toast.LENGTH_LONG).show()
            }
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startMeasurement() { // AudioRecord 초기화 및 측정 시작, MediaRecorder로 녹음
        if (isRecording) return
        try {
            val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                Toast.makeText(requireContext(), "오디오 설정을 초기화할 수 없습니다", Toast.LENGTH_SHORT).show()
                return
            }
            resetMeasurementState()

            // MediaRecorder로 녹음 시작 (M4A 형식 - AI 분석 최적화)
            recordingFilePath = "${requireContext().externalCacheDir?.absolutePath}/noise_${System.currentTimeMillis()}.m4a"
            mediaRecorder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                MediaRecorder(requireContext())
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                // AAC_ADTS 형식 사용 - 순수 오디오 전용 AAC
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS) // M4A/AAC 오디오 전용
                } else {
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                }
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000) // 128kbps
                setAudioSamplingRate(44100) // 44.1kHz
                setOutputFile(recordingFilePath)
                prepare()
                start()
            }

            // AudioRecord로 데시벨 측정
            audioRecord = createBestAudioRecord(bufferSize)
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                Toast.makeText(requireContext(), "마이크를 초기화할 수 없습니다", Toast.LENGTH_SHORT).show()
                audioRecord?.release()
                audioRecord = null
                mediaRecorder?.stop()
                mediaRecorder?.release()
                mediaRecorder = null
                return
            }
            audioRecord?.startRecording()
            isRecording = true
            updateControlButton(true)
            recordingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                processAudioRecording(ShortArray(bufferSize))
            }
            startTimer()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "소음 측정을 시작할 수 없습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            stopMeasurement()
        }
    }

    private fun resetMeasurementState() { // 측정 상태 초기화
        maxDb = 0.0
        avgDb = 0.0
        dbList.clear()
        startTime = System.currentTimeMillis()
        baselineRms = null
        calibrationCount = 0
        calibrationRmsList.clear()
        smoothedDb = null
        rmsEma = null
    }

    private fun startTimer() { // 실시간 시간 업데이트
        timerJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            while (isRecording) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                val minutes = elapsed / 60
                val seconds = elapsed % 60
                binding.tvDuration.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                delay(1000)
            }
        }
    }
    private suspend fun processAudioRecording(buffer: ShortArray) { // 오디오 데이터 지속적으로 읽기
        var consecutiveErrors = 0
        while (coroutineContext.isActive && isRecording) {
            try {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: -1
                if (read > 0) {
                    consecutiveErrors = 0
                    processAudioBuffer(buffer, read)
                } else if (++consecutiveErrors > MAX_CONSECUTIVE_ERRORS) {
                    handleRecordingError("오디오 읽기 오류")
                    break
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (++consecutiveErrors > MAX_CONSECUTIVE_ERRORS) {
                    handleRecordingError("오디오 읽기 중 오류가 발생했습니다")
                    break
                }
            }
            delay(50) // 적절한 샘플링 간격 유지
        }
    }

    private suspend fun processAudioBuffer(buffer: ShortArray, read: Int) { // 오디오 버퍼 분석 및 데시벨 계산
        val rmsRaw = computeRms(buffer, read)
        rmsEma = rmsEma?.let { RMS_EMA_ALPHA * rmsRaw + (1 - RMS_EMA_ALPHA) * it } ?: rmsRaw
        if (baselineRms == null) {
            performCalibration(rmsEma!!)
            return
        }
        val displayDb = calculateAndSmoothDb(rmsEma!!)
        synchronized(dbList) {
            dbList.add(displayDb)
            if (displayDb > maxDb) maxDb = displayDb
            avgDb = dbList.average()
        }
        withContext(Dispatchers.Main) { updateUI(displayDb) }
    }

    private fun performCalibration(effRms: Double) { // 초기 환경 소음 기준점 설정
        calibrationRmsList.add(effRms)
        if (++calibrationCount >= CALIBRATION_FRAMES) {
            calibrationRmsList.sort()
            val idx = (calibrationRmsList.size * 0.25).toInt().coerceIn(0, calibrationRmsList.lastIndex)
            baselineRms = calibrationRmsList[idx].coerceAtLeast(10.0)
        }
    }

    private fun calculateAndSmoothDb(effRms: Double): Double { // RMS를 실제 데시벨로 변환
        val ratio = (effRms / baselineRms!!).coerceAtLeast(0.01)
        val relativeDb = 20.0 * log10(ratio)
        val scaleFactor = 1.5 // 민감도 조정
        val rawDb = BASE_DB + (relativeDb * scaleFactor)
        val clampedDb = rawDb.coerceIn(5.0, 85.0) // 실제 측정 가능한 범위로 제한
        smoothedDb = smoothedDb?.let { prev -> // 급격한 변화를 부드럽게 처리
            val diff = clampedDb - prev
            when {
                kotlin.math.abs(diff) < MIN_DELTA_THRESHOLD -> prev
                diff > 0 -> prev + kotlin.math.min(diff, MAX_RISE_PER_TICK)
                else -> kotlin.math.max(clampedDb, prev - RELEASE_RATE_PER_TICK)
            }
        } ?: clampedDb
        return smoothedDb!!
    }

    private suspend fun handleRecordingError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            stopMeasurement()
        }
    }

    @android.annotation.SuppressLint("MissingPermission")
    private fun createBestAudioRecord(bufferSize: Int): AudioRecord? { // UNPROCESSED > VOICE_RECOGNITION > MIC 순으로 시도
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) return null
        listOf(
            MediaRecorder.AudioSource.UNPROCESSED,
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            MediaRecorder.AudioSource.MIC
        ).forEach { src ->
            try {
                val ar = AudioRecord(src, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize * 2)
                if (ar.state == AudioRecord.STATE_INITIALIZED) return ar
                ar.release()
            } catch (_: Exception) {}
        }
        return null
    }

    private fun stopMeasurement() {
        isRecording = false
        recordingJob?.cancel()
        recordingJob = null
        timerJob?.cancel()
        timerJob = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        updateControlButton(false)
    }

    private fun stopMeasurementAndShowButtons() { // 측정 종료 후 재생/완료 버튼 표시
        stopMeasurement()

        // 녹음 파일이 존재하면 재생 및 완료 버튼 표시
        val recordingFile = recordingFilePath?.let { File(it) }
        if (recordingFile != null && recordingFile.exists()) {
            binding.recordingActionsContainer.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "측정이 완료되었습니다", Toast.LENGTH_SHORT).show()
        } else {
            // 녹음 파일이 없으면 바로 소음일기 작성 페이지로 이동
            navigateToNoiseLogForm()
        }
    }

    private fun navigateToNoiseLogForm() { // 소음일기 작성 페이지로 이동 (완료 버튼 클릭 시)
        // 녹음 파일이 있으면 업로드 후 이동
        val recordingFile = recordingFilePath?.let { File(it) }
        if (recordingFile != null && recordingFile.exists()) {
            viewLifecycleOwner.lifecycleScope.launch {
                uploadRecordingFileAndNavigate(recordingFile)
            }
        } else {
            // 녹음 파일이 없으면 바로 이동
            moveToNoiseLogForm()
        }
    }

    private suspend fun uploadRecordingFileAndNavigate(recordingFile: File) {
        try {
            // 1️⃣ 로컬에 영구 저장
            saveRecordingToLocalStorage(recordingFile)

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "녹음 파일 업로드 중...", Toast.LENGTH_SHORT).show()
            }

            // 2️⃣ 서버에 업로드
            val result = recordingRepository.uploadRecording(recordingFile)
            result.onSuccess { response ->
                recordingFilePath = response.fileUrl // 업로드된 URL로 교체

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "업로드 완료! (로컬 저장됨)", Toast.LENGTH_SHORT).show()
                    moveToNoiseLogForm()
                }
            }.onFailure { e ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "업로드 실패: ${e.message} (로컬 저장됨)", Toast.LENGTH_SHORT).show()
                    // 실패해도 로컬에는 저장됨
                    moveToNoiseLogForm()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                    moveToNoiseLogForm()
                }
            }
        }
    }

    private fun moveToNoiseLogForm() {
        val bundle = Bundle().apply {
            putDouble("max_db", maxDb)
            putDouble("avg_db", avgDb)
            putLong("duration", (System.currentTimeMillis() - startTime) / 1000)
            putLong("measured_at", startTime)
            recordingFilePath?.let { putString("recording_url", it) }
        }

        findNavController().navigateTo(Destination.NOISE_LOG_FORM, bundle)
    }

    private fun togglePlayRecording() { // 녹음 파일 재생/정지
        if (recordingFilePath == null || !File(recordingFilePath!!).exists()) {
            Toast.makeText(requireContext(), "재생할 녹음 파일이 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        if (isPlaying) {
            stopPlayback()
        } else {
            startPlayback()
        }
    }

    private fun startPlayback() {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(recordingFilePath)
                prepare()
                setOnCompletionListener {
                    stopPlayback()
                }
                start()
            }
            isPlaying = true
            binding.btnPlayRecording.text = "재생 중지"
            binding.btnPlayRecording.setIconResource(android.R.drawable.ic_media_pause)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "재생 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        binding.btnPlayRecording.text = "녹음 재생"
        binding.btnPlayRecording.setIconResource(android.R.drawable.ic_media_play)
    }

    // ═══════════════════════════════
    // 로컬 저장 관련 함수
    // ═══════════════════════════════

    /**
     * 녹음 파일을 앱 내부 저장소에 영구 저장
     * 위치: /data/data/com.kau.ttokttok/files/recordings/
     */
    private suspend fun saveRecordingToLocalStorage(sourceFile: File): File = withContext(Dispatchers.IO) {
        try {
            // 앱 전용 저장소에 영구 저장 (앱 삭제 전까지 유지)
            val recordingsDir = File(requireContext().filesDir, "recordings")
            if (!recordingsDir.exists()) {
                recordingsDir.mkdirs()
            }

            val destFile = File(recordingsDir, "recording_${System.currentTimeMillis()}.m4a")
            sourceFile.copyTo(destFile, overwrite = true)

            android.util.Log.d("NoiseMeasurement", "✅ Recording saved locally: ${destFile.absolutePath}")
            destFile
        } catch (e: Exception) {
            android.util.Log.e("NoiseMeasurement", "❌ Failed to save recording locally", e)
            sourceFile // 실패 시 원본 반환
        }
    }

    private fun computeRms(buffer: ShortArray, read: Int) = // RMS(Root Mean Square) 계산
        sqrt(buffer.take(read).sumOf { it.toDouble() * it.toDouble() } / read)

    private fun updateUI(currentDb: Double) {
        binding.tvCurrentDb.text = currentDb.toInt().toString()
        binding.circularGauge.setProgress(currentDb.toFloat(), animate = true)
        binding.tvMaxDb.text = String.format(Locale.getDefault(), "%ddB", maxDb.toInt())
        binding.tvAvgDb.text = String.format(Locale.getDefault(), "%ddB", avgDb.toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopMeasurement()
        stopPlayback()
        _binding = null
    }

    companion object {
        private const val SAMPLE_RATE = 44100 // 샘플링 레이트
        private val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO // 모노 채널
        private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT // 16비트 PCM
        private const val CALIBRATION_FRAMES = 15 // 캘리브레이션 프레임 수
        private const val BASE_DB = 10.0 // 배경 소음 기준 데시벨
        private const val RELEASE_RATE_PER_TICK = 0.8 // 데시벨 감소 속도
        private const val MAX_RISE_PER_TICK = 3.0 // 데시벨 증가 최대값
        private const val MIN_DELTA_THRESHOLD = 0.3 // 최소 변화 임계값
        private const val RMS_EMA_ALPHA = 0.4 // RMS 지수 이동 평균 계수
        private const val MAX_CONSECUTIVE_ERRORS = 50 // 최대 연속 에러 허용 횟수
    }
}
