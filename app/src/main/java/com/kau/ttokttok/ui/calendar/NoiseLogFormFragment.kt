package com.kau.ttokttok.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.FragmentNoiseLogFormBinding
import com.kau.ttokttok.domain.model.NoiseLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoiseLogFormFragment : Fragment() {

    private var _binding: FragmentNoiseLogFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoiseLogViewModel by activityViewModels()

    // 측정 데이터
    private var logId: String? = null // 수정 모드일 때 로그 ID
    private var isEditMode = false // 수정 모드 여부
    private var maxDb = 0.0
    private var avgDb = 0.0
    private var duration = 0L
    private var measuredAt = Date()

    private var selectedNoiseType: String? = null
    private val noiseTypeButtonMap = mutableMapOf<String, MaterialButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            logId = it.getString(ARG_LOG_ID)
            isEditMode = logId != null
            maxDb = it.getDouble(ARG_MAX_DB, 0.0)
            avgDb = it.getDouble(ARG_AVG_DB, 0.0)
            duration = it.getLong(ARG_DURATION, 0)
            measuredAt = Date(it.getLong(ARG_MEASURED_AT, System.currentTimeMillis()))

            // 수정 모드일 때 기존 데이터 불러오기
            if (isEditMode) {
                selectedNoiseType = it.getString(ARG_NOISE_TYPE)
                val memo = it.getString(ARG_MEMO)
                // onViewCreated에서 UI에 설정됨
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoiseLogFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupNoiseTypeButtons()
        setupListeners()

        // 수정 모드일 때 기존 데이터 표시
        if (isEditMode) {
            arguments?.let {
                val memo = it.getString(ARG_MEMO)
                binding.etMemo.setText(memo)
                selectedNoiseType?.let { type -> selectNoiseType(type) }
            }
        }
    }

    private fun setupUI() {
        val dateFormat = SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREAN)
        binding.tvMeasuredTime.text = dateFormat.format(measuredAt)

        val minutes = duration / 60
        val seconds = duration % 60
        binding.tvMeasuredDuration.text = if (minutes > 0) {
            "${minutes}분 ${seconds}초"
        } else {
            "${seconds}초"
        }

        binding.tvMaxNoise.text = "${maxDb.toInt()}dB"
        binding.tvAvgNoise.text = "${avgDb.toInt()}dB"

        val suggestedType = getSuggestedNoiseType(avgDb)
        binding.tvAiSuggested.text = suggestedType
        binding.tvAiSuggested.setOnClickListener {
            selectNoiseType(suggestedType)
        }
    }

    private fun setupNoiseTypeButtons() {
        // 버튼과 타입을 맵으로 관리
        noiseTypeButtonMap.apply {
            put("발걸음", binding.chipStep)
            put("망치질", binding.chipHammer)
            put("가구 끄는 소리", binding.chipFurniture)
            put("음악 소리", binding.chipMusic)
            put("아이들 뛰는 소리", binding.chipKids)
            put("청소기 소리", binding.chipVacuum)
            put("기타", binding.chipEtc)
        }

        // 각 버튼에 클릭 리스너 설정
        noiseTypeButtonMap.forEach { (type, button) ->
            button.setOnClickListener { selectNoiseType(type) }
        }
    }

    private fun setupListeners() {
        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnGenerateAiDiary.setOnClickListener {
            if (validateInput()) {
                generateAiDiary()
            }
        }

        binding.btnSave.setOnClickListener {
            if (validateInput()) {
                saveNoiseLog()
            }
        }
    }

    private fun selectNoiseType(type: String) {
        selectedNoiseType = type

        // 모든 버튼 초기화 후 선택된 버튼만 강조
        noiseTypeButtonMap.forEach { (_, button) ->
            button.apply {
                strokeColor = ContextCompat.getColorStateList(requireContext(), android.R.color.black)
                strokeWidth = 1
            }
        }

        noiseTypeButtonMap[type]?.apply {
            strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            strokeWidth = 4
        }
    }

    private fun getSuggestedNoiseType(avgDb: Double): String = when {
        avgDb >= 60.0 -> "망치질"
        avgDb >= 50.0 -> "가구 끄는 소리"
        avgDb >= 45.0 -> "아이들 뛰는 소리"
        avgDb >= 40.0 -> "발걸음"
        avgDb >= 35.0 -> "음악 소리"
        else -> "청소기 소리"
    }

    private fun validateInput(): Boolean {
        if (selectedNoiseType == null) {
            Toast.makeText(requireContext(), "소음 유형을 선택해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.etMemo.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), "메모를 입력해주세요", Toast.LENGTH_SHORT).show()
            binding.etMemo.requestFocus()
            return false
        }

        return true
    }

    private fun generateAiDiary() {
        val memo = binding.etMemo.text.toString()
        val aiGeneratedDiary = buildString {
            append("[$selectedNoiseType] $memo\n\n")
            append("측정 시간: ${duration}초 동안 ")
            append("최대 ${maxDb.toInt()}dB, 평균 ${avgDb.toInt()}dB의 소음이 감지되었습니다. ")
            append("이러한 수준의 소음은 일상생활에 불편을 초래할 수 있으며, ")
            append("지속적으로 발생할 경우 층간소음 문제로 발전할 가능성이 있습니다.")
        }

        binding.etMemo.setText(aiGeneratedDiary)
        Toast.makeText(requireContext(), "AI 일기가 생성되었습니다", Toast.LENGTH_SHORT).show()
    }

    private fun saveNoiseLog() {
        val noiseLog = NoiseLog(
            id = logId, // 수정 모드일 때 기존 ID 유지
            noiseType = selectedNoiseType!!,
            maxDecibel = maxDb,
            avgDecibel = avgDb,
            memo = binding.etMemo.text.toString(),
            measuredAt = measuredAt,
            hasReport = false
        )

        if (isEditMode) {
            viewModel.updateLog(noiseLog)
            Toast.makeText(requireContext(), "소음 일기가 수정되었습니다", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.saveLog(noiseLog)
            Toast.makeText(requireContext(), "소음 일기가 저장되었습니다", Toast.LENGTH_SHORT).show()
        }

        // 저장한 날짜를 선택하여 해당 날짜의 로그를 표시
        viewModel.selectDate(measuredAt)

        // 캘린더 화면으로 돌아가기 (Navigator 방식)
        if (isEditMode) {
            // 수정 모드일 때는 바로 이전 화면(NoiseLogFragment)으로
            findNavController().popBackStack()
        } else {
            // 신규 등록일 때는 2번 pop (FormFragment -> MeasurementFragment -> LogFragment)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_LOG_ID = "log_id"
        private const val ARG_NOISE_TYPE = "noise_type"
        private const val ARG_MEMO = "memo"
        private const val ARG_MAX_DB = "max_db"
        private const val ARG_AVG_DB = "avg_db"
        private const val ARG_DURATION = "duration"
        private const val ARG_MEASURED_AT = "measured_at"

        // 신규 등록 모드
        fun newInstance(maxDb: Double, avgDb: Double, duration: Long, measuredAt: Long) =
            NoiseLogFormFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_MAX_DB, maxDb)
                    putDouble(ARG_AVG_DB, avgDb)
                    putLong(ARG_DURATION, duration)
                    putLong(ARG_MEASURED_AT, measuredAt)
                }
            }

        // 수정 모드
        fun newInstanceForEdit(log: NoiseLog) =
            NoiseLogFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOG_ID, log.id)
                    putString(ARG_NOISE_TYPE, log.noiseType)
                    putString(ARG_MEMO, log.memo)
                    putDouble(ARG_MAX_DB, log.maxDecibel)
                    putDouble(ARG_AVG_DB, log.avgDecibel)
                    putLong(ARG_DURATION, 0) // 수정 모드에서는 duration 사용 안함
                    putLong(ARG_MEASURED_AT, log.measuredAt.time)
                }
            }
    }
}
