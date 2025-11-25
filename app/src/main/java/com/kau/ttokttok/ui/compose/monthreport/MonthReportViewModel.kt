package com.kau.ttokttok.ui.compose.monthreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MonthReportUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val totalReports: Int = 0,
    val comparedToPrevious: Int = 0,
    val summaryText: String = "ERROR",

    val hourlyStats: Map<String, Int> = emptyMap(),
    val categoryStats: Map<String, Int> = emptyMap()
)

@HiltViewModel
class MonthReportViewModel @Inject constructor(
    private val useCase: GetMonthReportUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MonthReportUiState())
    val uiState: StateFlow<MonthReportUiState> = _uiState

    init {
        getMonthReport()
    }

    fun getMonthReport() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            useCase.invoke()
                .onSuccess { data ->
                    _uiState.update { current ->
                        current.copy(
                            totalReports = data.totalReportCount,
                            comparedToPrevious = data.changeRate.toInt(),
                            summaryText = data.aiAnalysisText,
                            hourlyStats = data.hourlyStats,
                            categoryStats = data.categoryStats
                        )
                    }
                }

                .onFailure {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = null
                        )

                        // TODO: 다이얼로그 추가
                    }
                }
        }
    }
}