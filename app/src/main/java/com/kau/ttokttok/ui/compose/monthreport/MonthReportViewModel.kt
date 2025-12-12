package com.kau.ttokttok.ui.compose.monthreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

sealed interface MonthReportEvent {
    data class ShowAlert(val title: String, val message: String) : MonthReportEvent
}

@HiltViewModel
class MonthReportViewModel @Inject constructor(
    private val useCase: GetMonthReportUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MonthReportUiState())
    val uiState: StateFlow<MonthReportUiState> = _uiState

    private val _events = MutableSharedFlow<MonthReportEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<MonthReportEvent> = _events.asSharedFlow()

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

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(MonthReportEvent.ShowAlert(
                        title = "조회 실패",
                        message = errorMessage ?: "알 수 없는 오류입니다."
                    ))
                }
        }
    }

    private fun emit(event: MonthReportEvent) {
        _events.tryEmit(event)
    }
}