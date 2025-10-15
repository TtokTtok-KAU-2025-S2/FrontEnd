package com.kau.ttokttok.ui.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kau.ttokttok.R
import com.kau.ttokttok.data.remote.NoiseInquiryRepository
import com.kau.ttokttok.domain.model.step2.InquiryResult
import com.kau.ttokttok.domain.model.step2.NoiseCategory
import com.kau.ttokttok.domain.model.step2.enums.InquiryStatus
import kotlinx.coroutines.launch

// 소음 탐색 메인 Fragment - 카테고리 선택 → 전송 → 응답 수집 → 결과 화면의 4페이지 화면을 관리
class NoiseInquiryFragment : Fragment() {

    // 최소 결과 진입 응답 수(총 응답 수가 더 적으면 총 응답 수로 대체) - 기본 5명
    private val MIN_COMPLETE_FOR_RESULT = 5

    // 현재 표시 중인 상태(중복 화면 전환 방지)
    private var lastShownStatus: InquiryStatus? = null

    // Fragment의 메인 뷰
    private var _currentView: View? = null
    private val currentView get() = _currentView!!

    // ViewModel과 Adapter 변수
    private lateinit var viewModel: NoiseInquiryViewModel
    private lateinit var responseAdapter: ResponseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewModel 초기화
        val factory = NoiseInquiryViewModelFactory(NoiseInquiryRepository())
        viewModel = ViewModelProvider(this, factory)[NoiseInquiryViewModel::class.java]

        // 어댑터 초기화
        responseAdapter = ResponseAdapter()

        // 최초 화면: 카테고리 선택
        val view = inflater.inflate(R.layout.activity_step2_category, container, false)
        _currentView = view
        setupCategoryView(view)

        // 상태 관찰하여 화면 전환
        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.let {
                    val newStatus = it.status
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

    private fun replaceCurrentView(newView: View) {
        val parent = _currentView?.parent as? ViewGroup
        parent?.let {
            val index = it.indexOfChild(_currentView)
            it.removeView(_currentView)
            it.addView(newView, index)
            _currentView = newView
        }
    }

    // 1) 카테고리 선택
    private fun showCategorySelection() {
        val parent = _currentView?.parent as? ViewGroup ?: return
        val newView = layoutInflater.inflate(R.layout.activity_step2_category, parent, false)
        replaceCurrentView(newView)
        setupCategoryView(newView)
    }

    private fun setupCategoryView(view: View) {
        // 뒤로가기 버튼
        view.findViewById<ImageButton>(R.id.btn_back)?.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val categories: List<NoiseCategory> = viewModel.getCategories()
        // 카테고리 버튼들
        view.findViewById<View>(R.id.category_living)?.setOnClickListener {
            categories.firstOrNull { it.id == "living" }?.let(viewModel::startInquiry)
        }
        view.findViewById<View>(R.id.category_pet)?.setOnClickListener {
            categories.firstOrNull { it.id == "pet" }?.let(viewModel::startInquiry)
        }
        view.findViewById<View>(R.id.category_machine)?.setOnClickListener {
            categories.firstOrNull { it.id == "machine" }?.let(viewModel::startInquiry)
        }
        view.findViewById<View>(R.id.category_plumbing)?.setOnClickListener {
            categories.firstOrNull { it.id == "plumbing" }?.let(viewModel::startInquiry)
        }
        view.findViewById<View>(R.id.category_external)?.setOnClickListener {
            categories.firstOrNull { it.id == "external" }?.let(viewModel::startInquiry)
        }
        view.findViewById<View>(R.id.category_other)?.setOnClickListener {
            categories.firstOrNull { it.id == "other" }?.let(viewModel::startInquiry)
        }
    }

    // 2) 전송 중 화면
    private fun showSending() {
        val parent = _currentView?.parent as? ViewGroup ?: return
        val newView = layoutInflater.inflate(R.layout.activity_step2_sending, parent, false)
        replaceCurrentView(newView)

        val tvCategoryName = newView.findViewById<TextView>(R.id.tv_category_name)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.category?.let { category ->
                    tvCategoryName?.text = category.name
                }
            }
        }
    }

    // 3) 응답 수집 화면
    private fun showResponses() {
        val parent = _currentView?.parent as? ViewGroup ?: return
        val newView = layoutInflater.inflate(R.layout.activity_step2_responses, parent, false)
        replaceCurrentView(newView)

        val rv = newView.findViewById<RecyclerView>(R.id.rv_responses)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = responseAdapter

        val btnViewResults = newView.findViewById<Button>(R.id.btn_view_results)
        val tvResponseCount = newView.findViewById<TextView>(R.id.tv_response_count)
        val progress = newView.findViewById<ProgressBar>(R.id.progress_responses)
        val cardComplete = newView.findViewById<View>(R.id.card_complete_button)

        btnViewResults?.setOnClickListener { viewModel.completeInquiry() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inquiryState.collect { inquiry ->
                inquiry?.let {
                    responseAdapter.updateResponses(it.responses)
                    val total = it.responses.size
                    val completed = viewModel.getCompletedResponseCount()
                    tvResponseCount?.text = "$completed/$total"
                    progress?.max = total
                    progress?.progress = completed

                    val threshold = if (total >= MIN_COMPLETE_FOR_RESULT) MIN_COMPLETE_FOR_RESULT else total
                    val canViewResult = completed >= threshold && threshold > 0
                    cardComplete?.visibility = if (canViewResult) View.VISIBLE else View.GONE
                    btnViewResults?.isEnabled = canViewResult
                }
            }
        }
    }

    // 4) 결과 화면
    private fun showResult() {
        val parent = _currentView?.parent as? ViewGroup ?: return
        val newView = layoutInflater.inflate(R.layout.activity_step2_result, parent, false)
        replaceCurrentView(newView)

        val btnBack = newView.findViewById<ImageButton>(R.id.btn_back)
        btnBack?.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        val result = viewModel.getResult()
        updateResultUI(newView, result)

        val btnSendToManagement = newView.findViewById<Button>(R.id.btn_send_to_management)
        val btnCancelSend = newView.findViewById<Button>(R.id.btn_cancel_send)
        val btnConfirmSend = newView.findViewById<Button>(R.id.btn_confirm_send)
        val btnSkip = newView.findViewById<Button>(R.id.btn_skip_report)
        val btnReturnSent = newView.findViewById<Button>(R.id.btn_return_to_main_sent)
        val btnReturnResolved = newView.findViewById<Button>(R.id.btn_return_to_main_resolved)
        val cardConfirm = newView.findViewById<View>(R.id.card_report_confirmation)
        val cardSent = newView.findViewById<View>(R.id.card_report_sent)

        btnSendToManagement?.setOnClickListener { cardConfirm?.visibility = View.VISIBLE }
        btnCancelSend?.setOnClickListener { cardConfirm?.visibility = View.GONE }
        btnConfirmSend?.setOnClickListener {
            cardConfirm?.visibility = View.GONE
            cardSent?.visibility = View.VISIBLE
        }
        btnSkip?.setOnClickListener {
            viewModel.resetInquiry()
            showCategorySelection()
        }
        btnReturnSent?.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        btnReturnResolved?.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun updateResultUI(view: View, result: InquiryResult) {
        view.findViewById<TextView>(R.id.tv_result_heard_count)?.text = result.heardCount.toString()
        view.findViewById<TextView>(R.id.tv_result_quiet_count)?.text = result.quietCount.toString()
        view.findViewById<TextView>(R.id.tv_result_sorry_count)?.text = result.sorryCount.toString()
        view.findViewById<TextView>(R.id.tv_total_responses)?.text = "• 총 응답: ${result.totalResponses}명"
    }
}
