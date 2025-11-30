package com.kau.ttokttok.ui.compose.noisevote.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.local.repository.NoiseVoteRepositoryImpl
import com.kau.ttokttok.data.remote.dto.noiseboard.res.Comment
import com.kau.ttokttok.data.remote.dto.noiseboard.res.VoteCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoiseVoteDetailUiState(
    val title: String = "",
    val buildingNumber: Int = 0,
    val reportedAt: String = "",
    val category: String = "",
    val voteCount: VoteCount? = null,
    val comments: List<Comment> = emptyList()
)

@HiltViewModel
class NoiseVoteDetailViewModel @Inject constructor(
    private val repository: NoiseVoteRepositoryImpl,
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
            Log.d("NoiseVoteDetailViewModel", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d("NoiseVoteDetailViewModel", "소음현황판 상세 데이터 로딩 시작")
            Log.d("NoiseVoteDetailViewModel", "요청 URL: GET /api/noise-reports/$noiseVoteId")

            when (val result = repository.getPostDetail(noiseVoteId)) {
                is NetworkResult.Success -> {
                    Log.d("NoiseVoteDetailViewModel", "✅ 소음현황판 상세 데이터 로딩 성공!")
                    Log.d("NoiseVoteDetailViewModel", "  - 게시글 ID: ${result.data.reportId}")
                    Log.d("NoiseVoteDetailViewModel", "  - Summary(AI 요약): ${result.data.summary}")
                    Log.d("NoiseVoteDetailViewModel", "  - 작성자: ${result.data.authorDong}동")
                    Log.d("NoiseVoteDetailViewModel", "  - 작성일: ${result.data.reportedAt}")
                    Log.d("NoiseVoteDetailViewModel", "  - 카테고리: ${result.data.category}")
                    Log.d("NoiseVoteDetailViewModel", "  - 투표 수 - 들었어요: ${result.data.voteCounts.HEARD}")
                    Log.d("NoiseVoteDetailViewModel", "  - 투표 수 - 못들었어요: ${result.data.voteCounts.NOT_HEARD}")
                    Log.d("NoiseVoteDetailViewModel", "  - 투표 수 - 조심할게요: ${result.data.voteCounts.BE_CAREFUL}")
                    Log.d("NoiseVoteDetailViewModel", "  - 댓글 수: ${result.data.comments.size}개")

                    val title = result.data.summary
                    val buildingNumber = result.data.authorDong
                    val reportedAt = result.data.reportedAt
                    val category = result.data.category
                    val voteCount = result.data.voteCounts
                    val comments = result.data.comments

                    _uiState.value = NoiseVoteDetailUiState(
                        title = title,
                        buildingNumber = buildingNumber,
                        reportedAt = reportedAt,
                        category = category,
                        voteCount = voteCount,
                        comments = comments
                    )

                    Log.d("NoiseVoteDetailViewModel", "소음현황판 상세 UI 업데이트 완료")
                }

                is NetworkResult.Error -> {
                    Log.e("NoiseVoteDetailViewModel", "❌ 소음현황판 상세 데이터 로딩 실패!")
                    Log.e("NoiseVoteDetailViewModel", "  - HTTP 코드: ${result.code}")
                    Log.e("NoiseVoteDetailViewModel", "  - 에러 메시지: ${result.message}")
                    result.exception?.let {
                        Log.e("NoiseVoteDetailViewModel", "  - Exception: ${it.message}", it)
                    }
                    _uiState.value = NoiseVoteDetailUiState()
                }
            }

            Log.d("NoiseVoteDetailViewModel", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        }
    }
}