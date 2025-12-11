package com.kau.ttokttok.ui.compose.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.auth.LoginUseCase
import com.kau.ttokttok.domain.usecase.auth.RequestTempPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginEvent {
    data object NavigateHome : LoginEvent
    data object NavigateSignup : LoginEvent
    data class ShowAlert(val title: String, val message: String) : LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val requestTempPasswordUseCase: RequestTempPasswordUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onClickLogin(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            loginUseCase.invoke(email, password)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false
                        )
                    }

                    emit(LoginEvent.NavigateHome)
                }

                .onFailure { e ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }

                    emit(LoginEvent.ShowAlert(
                        title = "로그인 실패",
                        message = e.message ?: "알 수 없는 오류입니다."
                    ))
                }
        }
    }

    fun onRequestTempPassword(email: String) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            requestTempPasswordUseCase.invoke(email)
                .onSuccess {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false
                        )
                    }

                    emit(
                        LoginEvent.ShowAlert(
                            title = "임시 비밀번호 발급 성공",
                            message = "이메일을 확인해주세요!"
                        )
                    )
                }

                .onFailure { throwable ->
                    val errorMessage = throwable.message ?: "ERROR"

                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(
                        LoginEvent.ShowAlert(
                            title = "임시 비밀번호 발급 실패",
                            message = errorMessage
                        )
                    )
                }
        }
    }

    private fun emit(event: LoginEvent) {
        _events.tryEmit(event)
    }
}