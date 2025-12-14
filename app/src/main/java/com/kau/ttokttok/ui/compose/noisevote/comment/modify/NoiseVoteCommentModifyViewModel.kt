package com.kau.ttokttok.ui.compose.noisevote.comment.modify

import androidx.lifecycle.*
import com.kau.ttokttok.domain.usecase.noisevote.ModifyCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoiseVoteCommentModifyUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val id: Long? = null,
    val commentContent: String? = null
)

sealed interface NoiseVoteCommentModifyEvent {
    data object OnSuccess : NoiseVoteCommentModifyEvent

    data class ShowAlert(val title: String, val message: String) : NoiseVoteCommentModifyEvent
}

@HiltViewModel
class NoiseVoteCommentModifyViewModel @Inject constructor(
    private val useCase: ModifyCommentUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val noiseVoteCommentId: Long = checkNotNull(savedStateHandle.get<Long>("noiseVoteCommentId"))
    private val noiseVoteCommentContent: String = checkNotNull(savedStateHandle.get<String>("noiseVoteCommentContent"))

    private val _uiState = MutableStateFlow(NoiseVoteCommentModifyUiState())
    val uiState: StateFlow<NoiseVoteCommentModifyUiState> = _uiState

    private val _events = MutableSharedFlow<NoiseVoteCommentModifyEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NoiseVoteCommentModifyEvent> = _events.asSharedFlow()

    init {
        loadComment()
    }

    fun onClickModify(content: String) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            useCase.invoke(
                id = noiseVoteCommentId,
                content = content
            )
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    emit(NoiseVoteCommentModifyEvent.OnSuccess)
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(NoiseVoteCommentModifyEvent.ShowAlert(
                        title = "변경 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    private fun loadComment() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            _uiState.update { after ->
                after.copy(
                    isLoading = false,
                    errorMessage = null,

                    commentContent = noiseVoteCommentContent
                )
            }
        }
    }

    private suspend fun emit(event: NoiseVoteCommentModifyEvent) {
        _events.emit(event)
    }
}