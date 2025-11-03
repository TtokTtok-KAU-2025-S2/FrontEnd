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
import kotlin.math.log10
import kotlin.math.sqrt

class NoiseMeasurementFragment : Fragment() {

    private var _binding: FragmentNoiseMeasurementBinding? = null
    private val binding get() = _binding!!

    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var recordingJob: Job? = null

    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT

    // 측정 통계
    private var maxDb = 0.0
    private var avgDb = 0.0
    private val dbList = mutableListOf<Double>()
    private var startTime = 0L

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

    private fun setupUI() {
        // 측정 버튼 클릭 리스너
        binding.btnControl.setOnClickListener {
            if (isRecording) {
                stopMeasurementAndNavigate()
            } else {
                checkPermissionAndStart()
            }
        }

        // 초기 상태 설정
        updateControlButton(false)
    }

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
    }

    private fun checkPermissionAndStart() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startMeasurement()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                Toast.makeText(
                    requireContext(),
                    "소음을 측정하기 위해 마이크 권한이 필요합니다",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startMeasurement() {
        if (isRecording) return

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        try {
            val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

            // 통계 초기화
            maxDb = 0.0
            avgDb = 0.0
            dbList.clear()
            startTime = System.currentTimeMillis()

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            audioRecord?.startRecording()
            isRecording = true
            updateControlButton(true)

            // Coroutine으로 Thread 대체
            recordingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val buffer = ShortArray(bufferSize)
                while (isActive && isRecording) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        val db = calculateDecibels(buffer, read)

                        // 통계 업데이트
                        synchronized(dbList) {
                            dbList.add(db)
                            if (db > maxDb) maxDb = db
                            avgDb = dbList.average()
                        }

                        withContext(Dispatchers.Main) {
                            updateUI(db)
                        }
                    }
                    delay(100)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "소음 측정을 시작할 수 없습니다", Toast.LENGTH_SHORT).show()
            stopMeasurement()
        }
    }

    private fun stopMeasurement() {
        isRecording = false
        recordingJob?.cancel()
        recordingJob = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        updateControlButton(false)
    }

    private fun stopMeasurementAndNavigate() {
        stopMeasurement()

        // 측정 데이터를 NoiseLogFormFragment로 전달
        val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000

        val formFragment = NoiseLogFormFragment.newInstance(
            maxDb = maxDb,
            avgDb = avgDb,
            duration = elapsedSeconds,
            measuredAt = startTime
        )

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, formFragment)
            .addToBackStack(null)
            .commit()

        Toast.makeText(requireContext(), "측정이 완료되었습니다", Toast.LENGTH_SHORT).show()
    }

    private fun calculateDecibels(buffer: ShortArray, read: Int): Double {
        val sum = buffer.take(read).sumOf { (it * it).toDouble() }
        val rms = sqrt(sum / read)
        val db = 20 * log10(rms / 32768.0) + 90 // 기준값 조정
        return db.coerceIn(0.0, 120.0)
    }

    private fun updateUI(currentDb: Double) {
        // 현재 데시벨 값
        binding.tvCurrentDb.text = currentDb.toInt().toString()

        // 최대 데시벨
        binding.tvMaxDb.text = String.format(Locale.getDefault(), "%ddB", maxDb.toInt())

        // 평균 데시벨
        binding.tvAvgDb.text = String.format(Locale.getDefault(), "%ddB", avgDb.toInt())

        // 경과 시간
        val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000
        val minutes = elapsedSeconds / 60
        val seconds = elapsedSeconds % 60
        binding.tvDuration.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopMeasurement()
        _binding = null
    }

    companion object {
        fun newInstance() = NoiseMeasurementFragment()
    }
}
