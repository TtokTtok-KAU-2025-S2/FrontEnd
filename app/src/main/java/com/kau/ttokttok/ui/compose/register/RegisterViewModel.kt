package com.kau.ttokttok.ui.compose.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface RegisterEvent {
    data object NavigateHome : RegisterEvent

    data class ShowAlert(val title: String, val message: String) : RegisterEvent
}
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _events = MutableSharedFlow<RegisterEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    fun onClickRegister(
        aptId: String,
        email: String,
        password: String,
        buildingNumber: String,
        unitNumber: String
    ) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            registerUseCase.invoke(aptId, email, password, buildingNumber, unitNumber)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    emit(RegisterEvent.ShowAlert(
                        title = "회원가입 성공",
                        message = "다시 로그인해주세요."
                    ))
                }

                .onFailure { e ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }

                    emit(RegisterEvent.ShowAlert(
                        title = "회원가입 실패",
                        message = e.message ?: "알 수 없는 오류입니다."
                    ))
                }
        }
    }

    private fun emit(event: RegisterEvent) {
        _events.tryEmit(event)
    }
}