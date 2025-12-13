package com.kau.ttokttok.ui.compose.preconsideration.writing

import androidx.lifecycle.*
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.usecase.preconsideration.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WritingPreConsiderationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val preConsiderationBoardDetail: PreConsiderationBoardDetail? = null
)

sealed interface WritingPreconsiderationEvent {
    data object Success: WritingPreconsiderationEvent

    data class ShowAlert(val title: String, val message: String) : WritingPreconsiderationEvent
}

@HiltViewModel
class WritingPreConsiderationViewModel @Inject constructor(
    private val createPostPreConsiderationUseCase: CreatePostPreConsiderationUseCase,
    private val loadPostDetailUseCase: LoadPostDetailPreConsiderationUseCase,
    private val modifyPostDetailUseCase: ModifyPostDetailPreConsiderationUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val preConsiderationId: Long? = savedStateHandle.get<Long>("preConsiderationId")
    val isEdit: Boolean get() = preConsiderationId != null

    private val _uiState = MutableStateFlow(WritingPreConsiderationUiState())
    val uiState: StateFlow<WritingPreConsiderationUiState> = _uiState

    private val _events = MutableSharedFlow<WritingPreconsiderationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<WritingPreconsiderationEvent> = _events.asSharedFlow()

    init {
        if (isEdit) {
            loadPostDetail()
        }
    }

    fun onClickCreate(
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            createPostPreConsiderationUseCase.invoke(
                title = title,
                content = content,
                noticeDate = noticeDate,
                noticeTime = noticeTime,
                noticeReason = noticeReason
            )
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    emit(WritingPreconsiderationEvent.Success)
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(WritingPreconsiderationEvent.ShowAlert(
                        title = "작성 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    fun onClickModify(
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            modifyPostDetailUseCase.invoke(
                id = preConsiderationId!!,
                title = title,
                content = content,
                noticeDate = noticeDate,
                noticeTime = noticeTime,
                noticeReason = noticeReason
            )
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    emit(WritingPreconsiderationEvent.Success)
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(
                        WritingPreconsiderationEvent.ShowAlert(
                            title = "작성 실패",
                            message = errorMessage ?: "ERROR"
                        )
                    )
                }
        }
    }

    private fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val id = preConsiderationId!!

            loadPostDetailUseCase.invoke(id)
                .onSuccess { data ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null,

                            preConsiderationBoardDetail = data
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

                    emit(WritingPreconsiderationEvent.ShowAlert(
                        title = "조회 실패",
                        message = errorMessage ?: "ERROR"
                    ))
                }
        }
    }

    private fun emit(event: WritingPreconsiderationEvent) {
        _events.tryEmit(event)
    }
}