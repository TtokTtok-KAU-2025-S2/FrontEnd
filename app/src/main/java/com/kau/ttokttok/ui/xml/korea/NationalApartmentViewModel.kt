package com.kau.ttokttok.ui.xml.korea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.report.res.GetApartmentStatsRes
import com.kau.ttokttok.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 전국 아파트 소음 통계 ViewModel
@HiltViewModel
class NationalApartmentViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadStats("") // 초기 로드 (전국 전체)
    }

    // 검색 실행
    fun search(keyword: String) {
        loadStats(keyword)
    }

    // API 호출 및 상태 업데이트
    private fun loadStats(keyword: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when (val result = repository.getApartmentStats(keyword)) {
                is NetworkResult.Success -> {
                    _uiState.value = if (result.data.apartments.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(result.data)
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.value = UiState.Error(
                        result.message ?: "데이터를 불러올 수 없습니다"
                    )
                }
            }
        }
    }

    // UI 상태
    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val data: GetApartmentStatsRes) : UiState()
        object Empty : UiState()
        data class Error(val message: String) : UiState()
    }
}

