package com.kau.ttokttok.ui.compose.preconsideration.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.local.repository.PreConsiderationRepositoryImpl
import com.kau.ttokttok.data.remote.dto.preconsideration.req.CreatePostPreConsiderationReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WritingPreConsiderationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface WritingPreconsiderationEvent {
    data object onSuccess: WritingPreconsiderationEvent
    data class ShowMessage(val message: String) : WritingPreconsiderationEvent
    data class ShowAlert(val title: String, val message: String) : WritingPreconsiderationEvent
}

@HiltViewModel
class WritingPreConsiderationViewModel @Inject constructor(
    private val repository: PreConsiderationRepositoryImpl
): ViewModel() {

    private val _uiState = MutableStateFlow(WritingPreConsiderationUiState())
    val uiState: StateFlow<WritingPreConsiderationUiState> = _uiState

    private val _events = MutableSharedFlow<WritingPreconsiderationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<WritingPreconsiderationEvent> = _events.asSharedFlow()

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

    private fun emit(event: WritingPreconsiderationEvent) {
        _events.tryEmit(event)
    }
}