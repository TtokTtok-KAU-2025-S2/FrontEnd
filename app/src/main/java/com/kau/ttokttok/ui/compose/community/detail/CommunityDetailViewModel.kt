package com.kau.ttokttok.ui.compose.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.usecase.community.GetPostDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class CommunityDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val communityBoardDetail: CommunityBoardDetail = CommunityBoardDetail(
        title = "제목 NULL",
        createdAt = LocalDateTime.now(),
        content = "내용 NULL",
        pictureUrl = null
    )
)

sealed interface CommunityDetailEvent {
    data class ShowAlert(val title: String, val message: String) : CommunityDetailEvent
}

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val useCase: GetPostDetailUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val communityId: Long = checkNotNull(savedStateHandle.get<Long>("communityId"))

    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState: StateFlow<CommunityDetailUiState> = _uiState

    private val _events = MutableSharedFlow<CommunityDetailEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<CommunityDetailEvent> = _events.asSharedFlow()

    init {
        getCommunityDetail(communityId)
    }

    fun getCommunityDetail(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            useCase.invoke(id)
                .onSuccess { data ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null,

                            communityBoardDetail = data
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

                    emit(CommunityDetailEvent.ShowAlert(
                        title = "조회 실패",
                        message = errorMessage ?: "알 수 없는 오류입니다."
                    ))
                }

        }
    }

    private fun emit(event: CommunityDetailEvent) {
        _events.tryEmit(event)
    }
}