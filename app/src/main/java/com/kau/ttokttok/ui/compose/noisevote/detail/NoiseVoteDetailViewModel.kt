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
            when (val result = repository.getPostDetail(noiseVoteId)) {
                is NetworkResult.Success -> {
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
                }

                is NetworkResult.Error -> {
                    _uiState.value = NoiseVoteDetailUiState()
                }
            }
        }
    }
}