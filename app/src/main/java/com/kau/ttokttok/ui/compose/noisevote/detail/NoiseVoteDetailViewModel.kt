package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.lifecycle.*
import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.domain.model.board.noisevote.*
import com.kau.ttokttok.domain.usecase.noisevote.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoiseVoteDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val noiseVoteBoardDetail: NoiseVoteBoardDetail? = null
)

sealed interface NoiseVoteDetailEvent {
    data class ShowAlert(val title: String, val message: String): NoiseVoteDetailEvent
}

@HiltViewModel
class NoiseVoteDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailNoiseVoteUseCase,
    private val postVoteUseCase: PostVoteUseCase,
    private val cancelVoteUseCase: CancelVoteUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val noiseVoteId: Long = checkNotNull(savedStateHandle.get<Long>("noiseVoteId"))

    private val _uiState = MutableStateFlow(NoiseVoteDetailUiState())
    val uiState: StateFlow<NoiseVoteDetailUiState> = _uiState

    private val _events = MutableSharedFlow<NoiseVoteDetailEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NoiseVoteDetailEvent> = _events.asSharedFlow()

    init {
        loadPostDetail()
    }

    fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            getPostDetailUseCase.invoke(noiseVoteId)
                .onSuccess { data ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null,

                            noiseVoteBoardDetail = data
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

                    emit(NoiseVoteDetailEvent.ShowAlert(
                        title = "조회 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    fun clickVote(voteType: NoiseVoteType) {
        if (_uiState.value.isLoading) return

        val current = _uiState.value.noiseVoteBoardDetail?.myVoteType

        if (current == voteType) {
            cancelVote()
        }

        else {
            postVote(voteType)
        }
    }

    fun addComment(content: String) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            addCommentUseCase(noiseVoteId, content)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(NoiseVoteDetailEvent.ShowAlert(
                        title = "댓글 추가 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            deleteCommentUseCase.invoke(comment.id)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(NoiseVoteDetailEvent.ShowAlert(
                        title = "댓글 삭제 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    private fun postVote(voteType: NoiseVoteType) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            postVoteUseCase.invoke(noiseVoteId, voteType)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(NoiseVoteDetailEvent.ShowAlert(
                        title = "투표 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    private fun cancelVote() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            cancelVoteUseCase.invoke(noiseVoteId)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(NoiseVoteDetailEvent.ShowAlert(
                        title = "투표 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    private fun emit(event: NoiseVoteDetailEvent) {
        _events.tryEmit(event)
    }
}