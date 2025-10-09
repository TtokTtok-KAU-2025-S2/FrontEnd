package com.kau.ttokttok.ui.compose.login

import androidx.lifecycle.ViewModel
import com.kau.ttokttok.data.local.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginEvent {
    data object NavigateHome : LoginEvent
    data object NavigateSignup : LoginEvent
    data class ShowMessage(val message: String) : LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    private fun emit(event: LoginEvent) {
        _events.tryEmit(event)
    }

    // TODO: 추가하기
    fun onClickLogin(id: String, pw: String) {

    }
    fun onClickSignUp() = emit(LoginEvent.NavigateSignup)
    fun onClickFindId() = emit(LoginEvent.ShowMessage("준비 중"))
    fun onClickFindPassword() = emit(LoginEvent.ShowMessage("준비 중"))
    fun onClickKaKao() = emit(LoginEvent.ShowMessage("준비 중"))
    fun onClickNaver() = emit(LoginEvent.ShowMessage("준비 중"))
}