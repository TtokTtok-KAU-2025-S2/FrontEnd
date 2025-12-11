package com.kau.ttokttok.ui.compose.preconsideration.writing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.repository.PreConsiderationRepositoryImpl
import com.kau.ttokttok.data.remote.dto.preconsideration.req.CreatePostPreConsiderationReq
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.usecase.preconsideration.LoadPostDetailPreConsiderationUseCase
import com.kau.ttokttok.domain.usecase.preconsideration.ModifyPostDetailPreConsiderationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WritingPreConsiderationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val preConsiderationBoardDetail: PreConsiderationBoardDetail? = null
)

sealed interface WritingPreconsiderationEvent {
    data object onSuccess: WritingPreconsiderationEvent
    data class ShowMessage(val message: String) : WritingPreconsiderationEvent
    data class ShowAlert(val title: String, val message: String) : WritingPreconsiderationEvent
}

@HiltViewModel
class WritingPreConsiderationViewModel @Inject constructor(
    private val repository: PreConsiderationRepositoryImpl,
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
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val req = CreatePostPreConsiderationReq(
                title = title,
                content = content,
                eventDate = noticeDate,
                eventTime = noticeTime,
                eventReason = noticeReason
            )

            when (val result = repository.createPost(req)) {
                is NetworkResult.Success -> {
                    emit(WritingPreconsiderationEvent.onSuccess)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }

                is NetworkResult.Error -> {
                    val message = result.message ?: result.exception?.message

                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)

                    emit(
                        WritingPreconsiderationEvent.ShowAlert(
                            title = "작성 실패",
                            message = message ?: "알 수 없는 오류입니다."
                        )
                    )
                }

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

            val result = modifyPostDetailUseCase.invoke(
                id = preConsiderationId!!,
                title = title,
                content = content,
                noticeDate = noticeDate,
                noticeTime = noticeTime,
                noticeReason = noticeReason
            )

            result
                .onSuccess {
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false
                        )
                    }

                    emit(WritingPreconsiderationEvent.onSuccess)
                }

                .onFailure { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )
                    }

                    emit(
                        WritingPreconsiderationEvent.ShowAlert(
                            title = "작성 실패",
                            message = response.message ?: "알 수 없는 오류입니다."
                        )
                    )
                }
        }
    }

    private fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            val id = preConsiderationId!!

            loadPostDetailUseCase.invoke(id)
                .onSuccess { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            preConsiderationBoardDetail = response
                        )
                    }
                }

                .onFailure { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )
                    }

                    // TODO: 다이얼로그 추가하기
                }
        }
    }

    private fun emit(event: WritingPreconsiderationEvent) {
        _events.tryEmit(event)
    }
}