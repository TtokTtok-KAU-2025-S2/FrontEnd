package com.kau.ttokttok.ui.calendar

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
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
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.FragmentNoiseMeasurementBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.coroutineContext
import kotlin.math.log10
import kotlin.math.sqrt

// 실시간 소음 측정 프래그먼트
class NoiseMeasurementFragment : Fragment() {

    private var _binding: FragmentNoiseMeasurementBinding? = null
    private val binding get() = _binding!!

    // 오디오 녹음
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var recordingJob: Job? = null
    private var timerJob: Job? = null // 시간 업데이트용

    // 측정 통계
    private var maxDb = 0.0
    private var avgDb = 0.0
    private val dbList = mutableListOf<Double>()
    private var startTime = 0L

    // 캘리브레이션 (초기 환경 소음 기준점 설정)
    private var baselineRms: Double? = null
    private var calibrationCount = 0
    private val calibrationRmsList = mutableListOf<Double>()
    private var baselineTargetSpl = NORMAL_BASELINE_DB
    private var absCalOffset = 0.0

    // 스무딩 (급격한 변화 평활화)
    private var smoothedDb: Double? = null
    private var rmsEma: Double? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startMeasurement()
        } else {
            Toast.makeText(requireContext(), "마이크 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoiseMeasurementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    // UI 초기 설정
    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            if (isRecording) stopMeasurement()
            parentFragmentManager.popBackStack()
        }
        binding.btnControl.setOnClickListener {
            if (isRecording) stopMeasurementAndNavigate() else checkPermissionAndStart()
        }
        updateControlButton(false)
    }

    // 측정 상태에 따라 UI 업데이트
    private fun updateControlButton(recording: Boolean) {
        binding.btnControl.setImageResource(
            if (recording) android.R.drawable.ic_media_pause
            else android.R.drawable.ic_btn_speak_now
        )
        binding.tvSubtitle.text = if (recording) {
            "측정을 완료하려면 버튼을 누르세요"
        } else {
            "측정 버튼을 눌러 시작하세요"
        }
        binding.statsContainer.visibility = if (recording) View.VISIBLE else View.INVISIBLE

        if (!recording) {
            binding.circularGauge.reset()
            binding.tvCurrentDb.text = "--"
        }
    }

    // 마이크 권한 확인 후 측정 시작
    private fun checkPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {
            startMeasurement()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(requireContext(), "소음을 측정하기 위해 마이크 권한이 필요합니다", Toast.LENGTH_LONG).show()
            }
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // AudioRecord 초기화 및 측정 시작
    private fun startMeasurement() {
        if (isRecording) return

        try {
            val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                Toast.makeText(requireContext(), "오디오 설정을 초기화할 수 없습니다", Toast.LENGTH_SHORT).show()
                return
            }

            resetMeasurementState()

            audioRecord = createBestAudioRecord(bufferSize)
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                Toast.makeText(requireContext(), "마이크를 초기화할 수 없습니다", Toast.LENGTH_SHORT).show()
                audioRecord?.release()
                audioRecord = null
                return
            }

            audioRecord?.startRecording()
            isRecording = true
            updateControlButton(true)

            // 오디오 데이터 처리
            recordingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                processAudioRecording(ShortArray(bufferSize))
            }

            // 시간 업데이트
            startTimer()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "소음 측정을 시작할 수 없습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            stopMeasurement()
        }
    }

    // 측정 상태 초기화
    private fun resetMeasurementState() {
        maxDb = 0.0
        avgDb = 0.0
        dbList.clear()
        startTime = System.currentTimeMillis()
        baselineRms = null
        calibrationCount = 0
        calibrationRmsList.clear()
        smoothedDb = null
        absCalOffset = 0.0
        rmsEma = null
    }

    // 실시간 시간 업데이트
    private fun startTimer() {
        timerJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            while (isRecording) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                val minutes = elapsed / 60
                val seconds = elapsed % 60
                binding.tvDuration.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                delay(1000) // 1초마다 업데이트
            }
        }
    }

    // 오디오 데이터 지속적으로 읽기
    private suspend fun processAudioRecording(buffer: ShortArray) {
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
            delay(RECORDING_DELAY_MS)
        }
    }

    // 오디오 버퍼 분석 및 데시벨 계산
    private suspend fun processAudioBuffer(buffer: ShortArray, read: Int) {
        // RMS 값 계산 및 지수 이동 평균 적용
        val rmsRaw = computeRms(buffer, read)
        rmsEma = rmsEma?.let { RMS_EMA_ALPHA * rmsRaw + (1 - RMS_EMA_ALPHA) * it } ?: rmsRaw

        // 캘리브레이션 미완료 시 수행
        if (baselineRms == null) {
            performCalibration(rmsEma!!)
            return
        }

        // 데시벨 계산 및 통계 업데이트
        val displayDb = calculateAndSmoothDb(rmsEma!!)
        val peakForMax = calculatePeakDb(computePeakAbs(buffer, read))

        synchronized(dbList) {
            dbList.add(displayDb)
            if (peakForMax > maxDb) maxDb = peakForMax
            avgDb = dbList.average()
        }

        withContext(Dispatchers.Main) { updateUI(displayDb) }
    }

    // 초기 환경 소음 기준점 설정
    private suspend fun performCalibration(effRms: Double) {
        calibrationRmsList.add(effRms)
        if (++calibrationCount >= CALIBRATION_FRAMES) {
            calibrationRmsList.sort()
            // 하위 25% 값을 기준점으로 설정
            val idx = (calibrationRmsList.size * 0.25).toInt().coerceIn(0, calibrationRmsList.lastIndex)
            baselineRms = calibrationRmsList[idx].coerceAtLeast(1.0)

            val estAbsDbRaw = 20.0 * log10(baselineRms!!)
            baselineTargetSpl = if (estAbsDbRaw < 30.0) QUIET_BASELINE_DB else NORMAL_BASELINE_DB
            absCalOffset = baselineTargetSpl - estAbsDbRaw
        }
        delay(RECORDING_DELAY_MS)
    }

    // RMS를 데시벨로 변환하고 스무딩 적용
    private fun calculateAndSmoothDb(effRms: Double): Double {
        val ratio = (effRms / baselineRms!!).coerceAtLeast(1e-6)
        val relDelta = 20.0 * log10(ratio) * if (ratio < 1) QUIET_SENSITIVITY else LOUD_SENSITIVITY
        val relDb = baselineTargetSpl + relDelta
        val absDb = 20.0 * log10(effRms.coerceAtLeast(1.0)) + absCalOffset
        val rawDb = (relDb * 0.85 + absDb * 0.15).coerceIn(0.0, 90.0)

        // 급격한 변화를 부드럽게 처리
        smoothedDb = smoothedDb?.let { prev ->
            val diff = rawDb - prev
            when {
                kotlin.math.abs(diff) < MIN_DELTA_THRESHOLD -> prev
                diff > 0 -> prev + kotlin.math.min(diff, MAX_RISE_PER_TICK)
                else -> kotlin.math.max(rawDb, prev - RELEASE_RATE_PER_TICK)
            }
        } ?: rawDb

        return smoothedDb!!.coerceIn(0.0, 80.0)
    }

    // 피크 데시벨 계산 (최대값 추적용)
    private fun calculatePeakDb(peakAbs: Double): Double {
        val ratio = (peakAbs / baselineRms!!).coerceAtLeast(1e-6)
        val relDelta = 20.0 * log10(ratio) * if (ratio < 1) QUIET_SENSITIVITY else LOUD_SENSITIVITY
        val relDb = baselineTargetSpl + relDelta
        val absDb = 20.0 * log10(peakAbs.coerceAtLeast(1.0)) + absCalOffset
        return (relDb * 0.85 + absDb * 0.15).coerceIn(0.0, 90.0)
    }

    private suspend fun handleRecordingError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            stopMeasurement()
        }
    }

    // 가장 적합한 AudioRecord 생성 (UNPROCESSED > VOICE_RECOGNITION > MIC)
    @android.annotation.SuppressLint("MissingPermission")
    private fun createBestAudioRecord(bufferSize: Int): AudioRecord? {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) return null

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
        updateControlButton(false)
    }

    // 측정 종료 후 결과 화면으로 이동
    private fun stopMeasurementAndNavigate() {
        stopMeasurement()

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, NoiseLogFormFragment.newInstance(
                maxDb = maxDb,
                avgDb = avgDb,
                duration = (System.currentTimeMillis() - startTime) / 1000,
                measuredAt = startTime
            ))
            .addToBackStack(null)
            .commit()

        Toast.makeText(requireContext(), "측정이 완료되었습니다", Toast.LENGTH_SHORT).show()
    }

    // RMS(Root Mean Square) 계산 - 오디오 신호의 평균 크기
    private fun computeRms(buffer: ShortArray, read: Int) =
        sqrt(buffer.take(read).sumOf { it.toDouble() * it.toDouble() } / read)

    // 피크 절대값 계산 (최대 음량 추적용)
    private fun computePeakAbs(buffer: ShortArray, read: Int) =
        buffer.take(read).maxOfOrNull { kotlin.math.abs(it.toInt()) }?.toDouble() ?: 0.0
    
    private fun updateUI(currentDb: Double) {
        binding.tvCurrentDb.text = currentDb.toInt().toString()
        binding.circularGauge.setProgress(currentDb.toFloat(), animate = true)
        binding.tvMaxDb.text = String.format(Locale.getDefault(), "%ddB", maxDb.toInt())
        binding.tvAvgDb.text = String.format(Locale.getDefault(), "%ddB", avgDb.toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopMeasurement()
        _binding = null
    }

    companion object {
        // 오디오 설정
        private const val SAMPLE_RATE = 44100
        private val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

        // 캘리브레이션
        private const val CALIBRATION_FRAMES = 15
        private const val QUIET_BASELINE_DB = 15.0
        private const val NORMAL_BASELINE_DB = 25.0

        // 스무딩
        private const val QUIET_SENSITIVITY = 1.0
        private const val LOUD_SENSITIVITY = 1.3
        private const val RELEASE_RATE_PER_TICK = 0.8
        private const val MAX_RISE_PER_TICK = 3.0
        private const val MIN_DELTA_THRESHOLD = 0.3
        private const val RMS_EMA_ALPHA = 0.4

        // 에러 핸들링
        private const val MAX_CONSECUTIVE_ERRORS = 50
        private const val RECORDING_DELAY_MS = 100L

        fun newInstance() = NoiseMeasurementFragment()
    }
}
