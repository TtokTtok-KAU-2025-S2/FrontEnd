package com.kau.ttokttok.ui.step2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kau.ttokttok.data.remote.NoiseInquiryRepository
import com.kau.ttokttok.databinding.ActivityStep2CategoryBinding
import com.kau.ttokttok.databinding.ActivityStep2SendingBinding
import com.kau.ttokttok.databinding.ActivityStep2ResponsesBinding
import com.kau.ttokttok.databinding.ActivityStep2ResultBinding
import com.kau.ttokttok.domain.model.InquiryStatus
import kotlinx.coroutines.launch

/**
 * 소음 탐색 메인 액티비티
 * 카테고리 선택 → 전송 → 응답 수집 → 결과 화면의 4단계 화면을 관리
 */
class NoiseInquiryActivity : AppCompatActivity() {

    // 최소 결과 진입 응답 수 (총 응답 수가 더 적으면 총 응답 수로 대체) - 기본 5명
    private val MIN_COMPLETE_FOR_RESULT = 5

    // 현재 표시 중인 상태 (중복 화면 전환 방지)
    private var lastShownStatus: InquiryStatus? = null

    // ViewBinding을 위한 변수들 (각 단계별로 다른 레이아웃 사용)
    private var categoryBinding: ActivityStep2CategoryBinding? = null
    private var sendingBinding: ActivityStep2SendingBinding? = null
    private var responsesBinding: ActivityStep2ResponsesBinding? = null
    private var resultBinding: ActivityStep2ResultBinding? = null

    // ViewModel과 Adapter 변수
    private val viewModel: NoiseInquiryViewModel by viewModels {
        NoiseInquiryViewModelFactory(NoiseInquiryRepository())
    }
    private lateinit var responseAdapter: ResponseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // RecyclerView 어댑터 초기화
        setupAdapter()

        // ViewModel 상태 관찰 시작
        observeViewModel()

        // 초기 화면 (카테고리 선택) 설정
        showCategorySelection()
    }

    /**
     * RecyclerView 어댑터 초기화
     */
    private fun setupAdapter() {
        responseAdapter = ResponseAdapter()
    }

    /**
     * ViewModel의 상태 변화를 관찰하는 함수
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.let {
                    val newStatus = it.status
                    if (newStatus != lastShownStatus) {
                        lastShownStatus = newStatus
                        when (newStatus) {
                            InquiryStatus.CATEGORY_SELECTION -> showCategorySelection()
                            InquiryStatus.SENDING -> showSending()
                            InquiryStatus.RESPONSES -> showResponses()
                            InquiryStatus.RESULT -> showResult()
                        }
                    }
                }
            }
        }
    }


}