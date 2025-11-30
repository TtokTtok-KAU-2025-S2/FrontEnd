package com.kau.ttokttok.ui.compose.noisevote.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoardDetail
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.domain.usecase.noisevote.AddCommentUseCase
import com.kau.ttokttok.domain.usecase.noisevote.CancelVoteUseCase
import com.kau.ttokttok.domain.usecase.noisevote.DeleteCommentUseCase
import com.kau.ttokttok.domain.usecase.noisevote.GetPostDetailNoiseVoteUseCase
import com.kau.ttokttok.domain.usecase.noisevote.ModifyCommentUseCase
import com.kau.ttokttok.domain.usecase.noisevote.PostVoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoiseVoteDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val noiseVoteBoardDetail: NoiseVoteBoardDetail? = null,

    val selectedVote: NoiseVoteType? = null
)

@HiltViewModel
class NoiseVoteDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailNoiseVoteUseCase,
    private val postVoteUseCase: PostVoteUseCase,
    private val cancelVoteUseCase: CancelVoteUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val modifyCommentUseCase: ModifyCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val noiseVoteId: Long = checkNotNull(savedStateHandle.get<Long>("noiseVoteId"))

    private val _uiState = MutableStateFlow(NoiseVoteDetailUiState())
    val uiState: StateFlow<NoiseVoteDetailUiState> = _uiState

    init {
        loadPostDetail()
    }

    fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            getPostDetailUseCase.invoke(noiseVoteId)
                .onSuccess { data ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            noiseVoteBoardDetail = data
                        )
                    }

                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun clickVote(voteType: NoiseVoteType) {
        if (_uiState.value.isLoading) return

        val current = _uiState.value.selectedVote

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
                    isLoading = true
                )
            }

            addCommentUseCase(noiseVoteId, content)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun modifyComment(comment: Comment) {
        viewModelScope.launch {
            _uiState.update { current->
                current.copy(
                    isLoading = true
                )
            }

            modifyCommentUseCase.invoke(comment)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = true
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            deleteCommentUseCase.invoke(comment.id)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun postVote(voteType: NoiseVoteType) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            val result = postVoteUseCase.invoke(noiseVoteId, voteType)
                .onSuccess { data ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            selectedVote = data.voteType
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun cancelVote() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            val result = cancelVoteUseCase.invoke(noiseVoteId)
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            selectedVote = null,
                        )
                    }

                    loadPostDetail()
                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }

            Log.d("viewModel", "$result")
        }
    }
}