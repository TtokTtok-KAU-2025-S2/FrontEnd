package com.kau.ttokttok.ui.compose.noisevote.comment.modify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.noisevote.ModifyCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
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
                    isLoading = true
                )
            }

            useCase.invoke(
                id = noiseVoteCommentId,
                content = content
            )
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false
                        )
                    }

                    emit(NoiseVoteCommentModifyEvent.OnSuccess)
                }

                .onFailure { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )
                    }

                    // TODO: 다이얼로그 추가
                }
        }
    }

    private fun loadComment() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            _uiState.update { after ->
                after.copy(
                    isLoading = false,
                    commentContent = noiseVoteCommentContent
                )
            }
        }
    }

    private suspend fun emit(event: NoiseVoteCommentModifyEvent) {
        _events.emit(event)
    }
}