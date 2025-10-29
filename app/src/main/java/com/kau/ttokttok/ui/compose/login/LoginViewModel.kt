package com.kau.ttokttok.ui.compose.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.repository.AuthRepository
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

) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    private fun emit(event: LoginEvent) {
        _events.tryEmit(event)
    }

    fun onClickLogin(id: String, pw: String) {
        viewModelScope.launch {
            try {
                // TODO: 나중에 repository로 부르기
                _events.emit(LoginEvent.NavigateHome)

                throw Exception("테스트용 실패")
            } catch (e: Exception) {
                emit(LoginEvent.ShowAlert("로그인 실패", e.message ?: "알 수 없는 오류입니다."))
            }
        }
    }

    fun onClickSignUp() = emit(LoginEvent.NavigateSignup)
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
}