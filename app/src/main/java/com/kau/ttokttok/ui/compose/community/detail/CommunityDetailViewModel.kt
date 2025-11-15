package com.kau.ttokttok.ui.compose.community.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
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
        content = "내용 NULL",
        author = "관리자"
    )
)

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
): ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState: StateFlow<CommunityDetailUiState> = _uiState

    init {
        getCommunityDetail()
    }

    fun getCommunityDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
        }
    }
}