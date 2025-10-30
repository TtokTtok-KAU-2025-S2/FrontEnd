package com.kau.ttokttok.ui.compose.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginEvent {
    data object NavigateHome : LoginEvent
    data object NavigateSignup : LoginEvent
    data class ShowMessage(val message: String) : LoginEvent
    data class ShowAlert(val title: String, val message: String) : LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onClickLogin(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                when (val result = authUseCase.login(email, password)) {
                    is NetworkResult.Success -> {
                        emit(LoginEvent.NavigateHome)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }

                    is NetworkResult.Error -> {
                        val message = result.message ?: result.exception?.message

                        _uiState.value =
                            _uiState.value.copy(isLoading = false, errorMessage = message)

                        emit(
                            LoginEvent.ShowAlert(
                                title = "로그인 실패",
                                message = message ?: "알 수 없는 오류입니다."
                            )
                        )
                    }
                }
            } catch (e: IllegalArgumentException) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(LoginEvent.ShowAlert("입력 오류", e.message ?: "잘못된 입력입니다."))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(LoginEvent.ShowAlert("로그인 실패", e.message ?: "알 수 없는 오류입니다."))
            }
        }
    }

    fun onClickRegister(
        email: String,
        password: String,
        buildingNumber: String,
        unitNumber: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                when (val r = authUseCase.register(email, password, buildingNumber, unitNumber)) {
                    is NetworkResult.Success -> {
                        
                        emit(LoginEvent.NavigateHome)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }

                    is NetworkResult.Error -> {
                        val message = r.message ?: r.exception?.message ?: "회원가입에 실패했습니다."

                        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
                        emit(LoginEvent.ShowAlert("회원가입 실패", message))
                    }
                }
            } catch (e: IllegalArgumentException) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(LoginEvent.ShowAlert("회원가입 실패", e.message ?: "잘못된 입력입니다."))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(LoginEvent.ShowAlert("회원가입 실패", e.message ?: "알 수 없는 오류입니다."))
            }
        }
    }
    fun onClickFindId() = emit(LoginEvent.ShowMessage("준비 중"))
    fun onClickFindPassword() = emit(LoginEvent.ShowMessage("준비 중"))

    // TODO: 추후 필요시 구현하기!
    fun onClickKaKao() {
        viewModelScope.launch {
            _events.emit(LoginEvent.ShowAlert("준비 중", "카카오 로그인은 아직 구현되지 않았습니다."))
        }
    }
    fun onClickNaver() {
        viewModelScope.launch {
            _events.emit(LoginEvent.ShowAlert("준비 중", "네이버 로그인은 아직 구현되지 않았습니다."))
        }
    }

    private fun emit(event: LoginEvent) {
        _events.tryEmit(event)
    }
}