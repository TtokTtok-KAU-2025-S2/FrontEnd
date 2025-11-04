package com.kau.ttokttok.ui.compose.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.domain.usecase.AuthUseCase
import com.kau.ttokttok.ui.compose.login.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface RegisterEvent {
    data object NavigateHome : RegisterEvent
    data class ShowMessage(val message: String) : RegisterEvent
    data class ShowAlert(val title: String, val message: String) : RegisterEvent
}
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _events = MutableSharedFlow<RegisterEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

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

                        emit(RegisterEvent.NavigateHome)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }

                    is NetworkResult.Error -> {
                        val message = r.message ?: r.exception?.message ?: "회원가입에 실패했습니다."

                        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
                        emit(RegisterEvent.ShowAlert("회원가입 실패", message))
                    }
                }
            } catch (e: IllegalArgumentException) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(RegisterEvent.ShowAlert("회원가입 실패", e.message ?: "잘못된 입력입니다."))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(RegisterEvent.ShowAlert("회원가입 실패", e.message ?: "알 수 없는 오류입니다."))
            }
        }
    }

    private fun emit(event: RegisterEvent) {
        _events.tryEmit(event)
    }
}