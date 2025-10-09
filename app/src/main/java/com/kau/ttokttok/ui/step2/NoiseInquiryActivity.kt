package com.kau.ttokttok.ui.step2

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.data.remote.NoiseInquiryRepository
import com.kau.ttokttok.databinding.ActivityStep2CategoryBinding
import com.kau.ttokttok.databinding.ActivityStep2SendingBinding
import com.kau.ttokttok.databinding.ActivityStep2ResponsesBinding
import com.kau.ttokttok.databinding.ActivityStep2ResultBinding
import com.kau.ttokttok.domain.model.step2.InquiryResult
import com.kau.ttokttok.domain.model.step2.NoiseCategory
import com.kau.ttokttok.domain.model.step2.enums.InquiryStatus
import kotlinx.coroutines.launch

// 소음 탐색 메인 액티비티 - 카테고리 선택 → 전송 → 응답 수집 → 결과 화면의 4페이지 화면을 관리
class NoiseInquiryActivity : AppCompatActivity() {

    // 최소 결과 진입 응답 수(총 응답 수가 더 적으면 총 응답 수로 대체) - 기본 5명
    private val MIN_COMPLETE_FOR_RESULT = 5

    // 현재 표시 중인 상태(중복 화면 전환 방지)
    private var lastShownStatus: InquiryStatus? = null

    // ViewBinding을 위한 변수들 (각 페이지별로 다른 레이아웃 사용)
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

    // RecyclerView 어댑터 초기화
    private fun setupAdapter() {
        responseAdapter = ResponseAdapter()
    }

    // ViewModel의 상태 변화를 관찰하는 함수
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.let {
                    val newStatus: InquiryStatus = it.status
                    // 중복 화면 전환 방지
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

    // 1페이지: 카테고리 선택 페이지 표시
    private fun showCategorySelection() {
        clearBindings()
        categoryBinding = ActivityStep2CategoryBinding.inflate(layoutInflater)
        setContentView(categoryBinding!!.root)

        // 카테고리 버튼들 설정
        setupCategoryButtons(viewModel.getCategories())

        // 뒤로가기 버튼 클릭 리스너
        categoryBinding!!.btnBack.setOnClickListener {
            finish() // 액티비티 종료
        }
    }

    // 카테고리 버튼들 설정
    private fun setupCategoryButtons(categories: List<NoiseCategory>) {
        categoryBinding?.let { binding ->
            // 각 카테고리 버튼에 클릭 리스너 설정
            binding.categoryLiving.setOnClickListener {
                categories.firstOrNull { it.id == "living" }?.let { category ->
                    viewModel.startInquiry(category)
                }
            }

            binding.categoryPet.setOnClickListener {
                categories.firstOrNull { it.id == "pet" }?.let { category ->
                    viewModel.startInquiry(category)
                }
            }

            binding.categoryMachine.setOnClickListener {
                categories.firstOrNull { it.id == "machine" }?.let { category ->
                    viewModel.startInquiry(category)
                }
            }

            binding.categoryPlumbing.setOnClickListener {
                categories.firstOrNull { it.id == "plumbing" }?.let { category ->
                    viewModel.startInquiry(category)
                }
            }

            binding.categoryExternal.setOnClickListener {
                categories.firstOrNull { it.id == "external" }?.let { category ->
                    viewModel.startInquiry(category)
                }
            }

            binding.categoryOther.setOnClickListener {
                categories.firstOrNull { it.id == "other" }?.let { category ->
                    viewModel.startInquiry(category)
                }
            }
        }
    }

    // 2페이지: 알림 전송 중 페이지 표시
    private fun showSending() {
        clearBindings()
        sendingBinding = ActivityStep2SendingBinding.inflate(layoutInflater)
        setContentView(sendingBinding!!.root)

        // 선택된 카테고리 정보 표시
        lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.category?.let { category ->
                    sendingBinding?.tvCategoryName?.text = category.name
                }
            }
        }
    }

    // 3페이지: 응답 수집 페이지 표시
    private fun showResponses() {
        clearBindings()
        responsesBinding = ActivityStep2ResponsesBinding.inflate(layoutInflater)
        setContentView(responsesBinding!!.root)

        // RecyclerView 설정
        setupResponsesRecyclerView()

        // 결과 보기 버튼 클릭 리스너
        responsesBinding!!.btnViewResults.setOnClickListener {
            viewModel.completeInquiry()
        }

        // 응답 목록 관찰하여 UI 업데이트
        lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.let {
                    responseAdapter.updateResponses(it.responses)
                    updateResponseProgress(it.responses.size, viewModel.getCompletedResponseCount())
                }
            }
        }
    }

    // 4페이지: 결과 페이지 표시
    private fun showResult() {
        clearBindings()
        resultBinding = ActivityStep2ResultBinding.inflate(layoutInflater)
        setContentView(resultBinding!!.root)

        // 결과 데이터로 UI 업데이트
        updateResultUI(viewModel.getResult())

        // 헤더 뒤로가기 버튼
        resultBinding!!.btnBack.setOnClickListener { finish() }

        // 관리사무소 전송/취소/확인 플로우
        resultBinding!!.btnSendToManagement.setOnClickListener {
            resultBinding!!.cardReportConfirmation.visibility = View.VISIBLE
        }
        resultBinding!!.btnCancelSend.setOnClickListener {
            resultBinding!!.cardReportConfirmation.visibility = View.GONE
        }
        resultBinding!!.btnConfirmSend.setOnClickListener {
            resultBinding!!.cardReportConfirmation.visibility = View.GONE
            resultBinding!!.cardReportSent.visibility = View.VISIBLE
        }

        // 전송 건너뛰기 → 새 탐색 시작
        resultBinding!!.btnSkipReport.setOnClickListener {
            viewModel.resetInquiry()
            showCategorySelection()
        }

        // 완료 카드에서 메인으로 복귀
        resultBinding!!.btnReturnToMainSent.setOnClickListener { finish() }
        resultBinding!!.btnReturnToMainResolved.setOnClickListener { finish() }
    }

    // 응답 수집 페이지의 RecyclerView 설정
    private fun setupResponsesRecyclerView() {
        responsesBinding?.let { binding ->
            binding.rvResponses.apply {
                layoutManager = LinearLayoutManager(this@NoiseInquiryActivity)
                adapter = responseAdapter
            }
        }
    }

    // 응답 진행률 업데이트
    private fun updateResponseProgress(total: Int, completed: Int) {
        responsesBinding?.let { binding ->
            binding.tvResponseCount.text = "$completed/$total"
            binding.progressResponses.max = total
            binding.progressResponses.progress = completed

            // 결과 보기 버튼 활성화 조건
            val threshold = if (total >= MIN_COMPLETE_FOR_RESULT) MIN_COMPLETE_FOR_RESULT else total
            val canViewResult = completed >= threshold && threshold > 0

            binding.cardCompleteButton.visibility = if (canViewResult) View.VISIBLE else View.GONE
            binding.btnViewResults.isEnabled = canViewResult
        }
    }

    // 결과 UI 업데이트
    private fun updateResultUI(result: InquiryResult) {
        resultBinding?.let { binding ->
            // 각 응답 유형별 개수 표시
            binding.tvResultHeardCount.text = result.heardCount.toString()
            binding.tvResultQuietCount.text = result.quietCount.toString()
            binding.tvResultSorryCount.text = result.sorryCount.toString()
            binding.tvTotalResponses.text = "• 총 응답: ${result.totalResponses}명"
        }
    }

    // 모든 바인딩 해제
    private fun clearBindings() {
        categoryBinding = null
        sendingBinding = null
        responsesBinding = null
        resultBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        // 메모리 누수 방지를 위해 바인딩 해제
        clearBindings()
    }
}
