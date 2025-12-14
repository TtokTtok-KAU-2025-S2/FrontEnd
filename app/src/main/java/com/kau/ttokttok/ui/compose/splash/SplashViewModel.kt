package com.kau.ttokttok.ui.compose.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface SplashEvent {
    data object MoveToLogin: SplashEvent
    data object MoveToMain: SplashEvent

    data class ShowAlert(val title: String, val message: String): SplashEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    private val _events = MutableSharedFlow<SplashEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<SplashEvent> = _events.asSharedFlow()

    init {
        checkAutoLogin()
    }

    fun checkAutoLogin() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            // 시간 측정
            val minDuration = 1800L
            val start = System.currentTimeMillis()

            // TODO: 자동 로그인 로직 넣기

            // 시간이 남았다면, 기다리기
            val elapsed = System.currentTimeMillis() - start
            val remain = minDuration - elapsed

            if (remain > 0) {
                delay(remain)
            }

            emit(SplashEvent.MoveToLogin)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    private fun emit(event: SplashEvent) {
        _events.tryEmit(event)
    }
}