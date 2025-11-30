package com.kau.ttokttok.ui.compose.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.usecase.community.GetPostDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class CommunityDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val communityBoardDetail: CommunityBoardDetail = CommunityBoardDetail(
        title = "제목 NULL",
        createdAt = LocalDateTime.now(),
        content = "내용 NULL"
    )
)

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val useCase: GetPostDetailUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val communityId: Long = checkNotNull(savedStateHandle.get<Long>("communityId"))

    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState: StateFlow<CommunityDetailUiState> = _uiState

    init {
        getCommunityDetail(communityId)
    }

    fun getCommunityDetail(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            useCase.invoke(id)
                .onSuccess { detail ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,

                        communityBoardDetail = detail
                    )

                }

                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message,
                    )

                    // TODO: 다이얼로그 추가
                }

        }
    }
}