package com.kau.ttokttok.ui.compose.preconsideration

import androidx.lifecycle.*
import com.kau.ttokttok.domain.usecase.preconsideration.GetPostsPreConsiderationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreConsiderationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val posts: List<PreConsiderationPost>? = null
)

sealed interface PreConsiderationEvent {
    data class ShowAlert(val title: String, val message: String): PreConsiderationEvent
}

@HiltViewModel
class PreConsiderationViewModel @Inject constructor(
    private val useCase: GetPostsPreConsiderationUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(PreConsiderationUiState())
    val uiState: StateFlow<PreConsiderationUiState> = _uiState

    private val _events = MutableSharedFlow<PreConsiderationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<PreConsiderationEvent> = _events.asSharedFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            useCase.invoke()
                .onSuccess { data ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null,
                            posts = data
                        )
                    }
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(PreConsiderationEvent.ShowAlert(
                        title = "글 조회 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }

        }
    }

    private fun emit(event: PreConsiderationEvent) {
        _events.tryEmit(event)
    }
}