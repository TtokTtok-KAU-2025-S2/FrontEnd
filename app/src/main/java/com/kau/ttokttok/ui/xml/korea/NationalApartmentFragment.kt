package com.kau.ttokttok.ui.xml.korea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.databinding.FragmentNationalDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 전국 아파트 소음 통계 화면
@AndroidEntryPoint
class NationalApartmentFragment : Fragment() {

    private var _binding: FragmentNationalDataBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NationalApartmentViewModel by viewModels()
    private val adapter = ApartmentStatsAdapter()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNationalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupListeners()
        observeUiState()
    }

    // 아파트 목록 RecyclerView 설정
    private fun setupRecyclerView() {
        binding.rvApartments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NationalApartmentFragment.adapter
        }
    }

    // 검색창 설정 (디바운싱 500ms)
    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.search(text?.toString()?.trim() ?: "")
            }
        }
    }

    // 뒤로가기 버튼 설정
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // ViewModel 상태 변화 감지
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is NationalApartmentViewModel.UiState.Loading -> showLoading()
                    is NationalApartmentViewModel.UiState.Success -> showSuccess(state)
                    is NationalApartmentViewModel.UiState.Empty -> showEmpty()
                    is NationalApartmentViewModel.UiState.Error -> showError(state.message)
                    else -> hideLoading()
                }
            }
        }
    }

    // 로딩 중 UI
    private fun showLoading() {
        binding.apply {
            tvEmpty.visibility = View.GONE
            rvApartments.visibility = View.GONE
        }
    }

    // 데이터 로드 성공 UI
    private fun showSuccess(state: NationalApartmentViewModel.UiState.Success) {
        binding.apply {
            tvEmpty.visibility = View.GONE
            rvApartments.visibility = View.VISIBLE
        }
        adapter.submitList(state.data.apartments)
    }

    // 검색 결과 없음 UI
    private fun showEmpty() {
        binding.apply {
            tvEmpty.visibility = View.VISIBLE
            rvApartments.visibility = View.GONE
        }
        adapter.submitList(emptyList())
    }

    // 에러 메시지 표시
    private fun showError(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // 로딩 숨김
    private fun hideLoading() {
        binding.rvApartments.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
}

