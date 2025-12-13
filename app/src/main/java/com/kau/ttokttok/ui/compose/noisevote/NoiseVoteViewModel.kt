package com.kau.ttokttok.ui.compose.noisevote

import androidx.lifecycle.*
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.usecase.noisevote.LoadPostsNoiseVoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoiseVoteUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val posts: List<NoiseVoteBoard> = emptyList()
)

sealed interface NoiseVoteEvent {
    data class ShowAlert(val title: String, val message: String): NoiseVoteEvent
}

@HiltViewModel
class NoiseVoteViewModel @Inject constructor(
    private val useCase: LoadPostsNoiseVoteUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(NoiseVoteUiState())
    val uiState: StateFlow<NoiseVoteUiState> = _uiState

    private val _events = MutableSharedFlow<NoiseVoteEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NoiseVoteEvent> = _events.asSharedFlow()

    init {
        loadPosts()

        savedStateHandle
            .getStateFlow("needRefresh", false)
            .onEach { needRefresh ->
                if (needRefresh) {
                    loadPosts()
                    savedStateHandle["needRefresh"] = false
                }
            }
            .launchIn(viewModelScope)
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

                    emit(NoiseVoteEvent.ShowAlert(
                        title = "조회 실패",
                        message = e.message ?: "ERROR"
                    ))
                }
        }
    }

    private fun emit(event: NoiseVoteEvent) {
        _events.tryEmit(event)
    }
}