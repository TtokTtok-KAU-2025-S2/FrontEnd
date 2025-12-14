package com.kau.ttokttok.ui.compose.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.R
import com.kau.ttokttok.domain.usecase.MainUseCase
import com.kau.ttokttok.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainSlide(
    val image: String,
    val title: String,
    val description: String,
    val buttonText: String,
    val destination: Destination,
    val imageRes: Int
)

data class MainUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val mainSlides: List<MainSlide> = listOf(
        MainSlide(
            image = "",
            title = "소음 일기",
            description = "캘린더에서 날짜를 선택하여\n 소음을 기록하세요",
            buttonText = "일기 보기",
            destination = Destination.NOISE_LOG,
            imageRes = R.drawable.main_calendar
        ),

        MainSlide(
            image = "",
            title = "소음 현황판",
            description = "이웃의 소음 리포트에\n 투표로 참여하세요",
            buttonText = "현황 보기",
            destination = Destination.NOISE_VOTE,
            imageRes = R.drawable.main_noisevote
        ),

        MainSlide(
            image = "",
            title = "월간 리포트",
            description = "AI가 분석한 우리 아파트의\n 소음 트렌드를 확인하세요",
            buttonText = "리포트 보기",
            destination = Destination.MONTH_REPORT,
            imageRes = R.drawable.main_monthreport
        )
    ),

    val buildingNumber: Int = 0,
    val unitNumber: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun getUserInformation() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            val buildingNumber = mainUseCase.getBuildingNumber()
            val unitNumber = mainUseCase.getUnitNumber()

            _uiState.update { after ->
                after.copy(
                    isLoading = false,

                    buildingNumber = buildingNumber,
                    unitNumber = unitNumber
                )
            }
        }
    }
}