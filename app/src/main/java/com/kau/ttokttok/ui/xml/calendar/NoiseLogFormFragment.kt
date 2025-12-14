package com.kau.ttokttok.ui.xml.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.kau.ttokttok.R
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.databinding.FragmentNoiseLogFormBinding
import com.kau.ttokttok.data.remote.dto.ai.res.toCategoryKorean
import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.AIRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class NoiseLogFormFragment : Fragment() {

    private var _binding: FragmentNoiseLogFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoiseLogViewModel by activityViewModels()

    @Inject
    lateinit var aiRepository: AIRepository

    // 측정 데이터
    private var logId: String? = null
    private var isEditMode = false
    private var maxDb = 0.0
    private var avgDb = 0.0
    private var duration = 0L
    private var measuredAt = Date()
    private var recordId: Long? = null // 녹음 파일 ID (AI 분석용)

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
            // ✅ NoiseMeasurementFragment 에서 넣어준 record_id 읽기
            val rawRecordId = it.getLong(ARG_RECORD_ID, -1L)
            recordId = rawRecordId.takeIf { id -> id != -1L }

            Log.d("NoiseLogFormFragment", "🔍 onCreate - arguments 확인")
            Log.d("NoiseLogFormFragment", "  - ARG_RECORD_ID(raw): $rawRecordId")
            Log.d("NoiseLogFormFragment", "  - recordId: $recordId")
            Log.d("NoiseLogFormFragment", "  - isEditMode: $isEditMode")

            // 수정 모드일 때 기존 데이터 불러오기
            if (isEditMode) {
                selectedNoiseType = it.getString(ARG_NOISE_TYPE)
                // memo는 onViewCreated에서 UI에 설정됨
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
        } else {
            // 신규 등록 모드: AI 카테고리 자동 분석
            recordId?.let { id ->
                Log.d("NoiseLogFormFragment", "")
                Log.d("NoiseLogFormFragment", "🎯 recordId 발견! AI 자동 분석 시작")
                Log.d("NoiseLogFormFragment", "  - recordId: $id")
                analyzeAICategory(id)
            } ?: run {
                Log.w("NoiseLogFormFragment", "")
                Log.w("NoiseLogFormFragment", "⚠️ recordId가 null입니다!")
                Log.w("NoiseLogFormFragment", "  - AI 분석을 건너뜁니다")
                Log.w("NoiseLogFormFragment", "  - 사용자가 직접 소음 유형을 선택해야 합니다")
                binding.tvAiSuggested.text = "직접 선택"
            }
        }
    }

    private fun setupUI() {
        Log.d("NoiseLogFormFragment", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d("NoiseLogFormFragment", "📋 소음일기 작성 화면 초기화")
        Log.d("NoiseLogFormFragment", "  - 수정 모드: $isEditMode")
        Log.d("NoiseLogFormFragment", "  - 최대 데시벨: $maxDb dB")
        Log.d("NoiseLogFormFragment", "  - 평균 데시벨: $avgDb dB")
        Log.d("NoiseLogFormFragment", "  - 측정 시간: $duration 초")
        Log.d("NoiseLogFormFragment", "  - recordId: $recordId")

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

        // AI 분석 중 표시
        if (!isEditMode && recordId != null) {
            binding.tvAiSuggested.text = "AI 분석 중..."
        }

        Log.d("NoiseLogFormFragment", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    }

    private fun setupNoiseTypeButtons() {
        Log.d("NoiseLogFormFragment", "")
        Log.d("NoiseLogFormFragment", "🔘 소음 유형 버튼 초기화")

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

        Log.d("NoiseLogFormFragment", "  - 등록된 버튼 수: ${noiseTypeButtonMap.size}개")
        noiseTypeButtonMap.keys.forEach { type ->
            Log.d("NoiseLogFormFragment", "    · $type")
        }

        // 각 버튼에 클릭 리스너 설정
        noiseTypeButtonMap.forEach { (type, button) ->
            button.setOnClickListener {
                Log.d("NoiseLogFormFragment", "")
                Log.d("NoiseLogFormFragment", "👆 사용자가 \"$type\" 버튼 클릭")
                selectNoiseType(type)
            }
        }

        Log.d("NoiseLogFormFragment", "  - 클릭 리스너 설정 완료")
    }

    private fun setupListeners() {
        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // 저장 버튼
        binding.btnSave.setOnClickListener {
            if (validateInput()) {
                saveNoiseLog()
            }
        }
    }

    private fun selectNoiseType(type: String) {
        Log.d("NoiseLogFormFragment", "")
        Log.d("NoiseLogFormFragment", "✅ 소음 유형 선택: $type")
        Log.d("NoiseLogFormFragment", "  - 이전 선택: $selectedNoiseType")

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
            Log.d("NoiseLogFormFragment", "  - UI 업데이트: \"$type\" 버튼 강조 완료")
        } ?: run {
            Log.e("NoiseLogFormFragment", "  ⚠️ 경고: \"$type\" 버튼을 찾을 수 없음!")
            Log.e("NoiseLogFormFragment", "  - 등록된 버튼: ${noiseTypeButtonMap.keys}")
        }

        Log.d("NoiseLogFormFragment", "  - 현재 선택: $selectedNoiseType")
    }

    /**
     * AI 카테고리 자동 분석
     * 녹음 파일을 서버 AI가 분석하여 소음 종류 판단
     */
    private fun analyzeAICategory(recordId: Long) {
        Log.d("NoiseLogFormFragment", "")
        Log.d("NoiseLogFormFragment", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d("NoiseLogFormFragment", "🤖 AI 카테고리 분석 시작")
        Log.d("NoiseLogFormFragment", "  - recordId: $recordId")
        Log.d("NoiseLogFormFragment", "  - API: POST /noise/ai/category")

        // 로딩 표시
        binding.tvAiSuggested.text = "AI 분석 중..."

        viewLifecycleOwner. lifecycleScope.launch {
            when (val result = aiRepository.getAICategory(recordId)) {
                is NetworkResult.Success -> {
                    val aiResult = result.data
                    val categoryKorean = aiResult.category.toCategoryKorean()

                    Log.d("NoiseLogFormFragment", "✅ AI 분석 성공!")
                    Log.d("NoiseLogFormFragment", "  - 카테고리: $categoryKorean (${aiResult.category})")
                    Log.d("NoiseLogFormFragment", "  - 음성 인식: ${aiResult.transcript}")
                    Log.d("NoiseLogFormFragment", "  - 분석 이유: ${aiResult.reason}")
                    Log.d("NoiseLogFormFragment", "  - 최대 데시벨: ${aiResult.dbMax}dB")
                    Log.d("NoiseLogFormFragment", "  - 평균 데시벨: ${aiResult.dbAvg}dB")
                    Log.d("NoiseLogFormFragment", "  - 측정 시간: ${aiResult.duration}초")

                    // UI 업데이트
                    binding.tvAiSuggested.text = categoryKorean

                    // 🎯 자동으로 카테고리 선택
                    Log.d("NoiseLogFormFragment", "  - 🤖 AI 추천 카테고리 자동 선택: $categoryKorean")
                    selectNoiseType(categoryKorean)

                    // AI 분석 이유를 TextView에 표시
                    binding.tvAiReason.apply {
                        text = "💡 분석 이유: ${aiResult.reason}"
                        visibility = View.VISIBLE
                    }

                    // AI 추천 텍스트 클릭 시 분석 이유 토글
                    binding.tvAiSuggested.setOnClickListener {
                        binding.tvAiReason.visibility =
                            if (binding.tvAiReason.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    }

                    Log.d("NoiseLogFormFragment", "  - UI 업데이트 완료")
                }

                is NetworkResult.Error -> {
                    Log.e("NoiseLogFormFragment", "❌ AI 분석 실패!")
                    Log.e("NoiseLogFormFragment", "  - 에러 코드: ${result.code}")
                    Log.e("NoiseLogFormFragment", "  - 에러 메시지: ${result.message}")

                    // 실패 시 기본 메시지
                    binding.tvAiSuggested.text = "AI 분석 실패"
                    binding.tvAiSuggested.setOnClickListener(null)
                    binding.tvAiReason.visibility = View.GONE

                    Toast.makeText(
                        requireContext(),
                        "AI 분석에 실패했습니다. 직접 선택해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Log.d("NoiseLogFormFragment", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        }
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


    private fun saveNoiseLog() {
        val noiseLog = NoiseLog(
            id = logId, // 수정 모드일 때 기존 ID 유지, 신규일 때 null
            noiseType = selectedNoiseType!!,
            maxDecibel = maxDb,
            avgDecibel = avgDb,
            memo = binding.etMemo.text.toString(),
            measuredAt = measuredAt,
            duration = duration, // 측정 화면에서 전달된 duration(초)
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

        // 캘린더 화면으로 돌아가기
        if (isEditMode) {
            findNavController().popBackStack()
        } else {
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
        private const val ARG_RECORD_ID = "record_id" // AI 분석용 녹음 파일 ID

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
