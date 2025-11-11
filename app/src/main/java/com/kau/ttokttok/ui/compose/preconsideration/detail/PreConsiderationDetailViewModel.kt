package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.local.repository.PreConsiderationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PreConsiderationDetailUiState(
    val title: String = "",
    val content: String = "",
    val noticeDate: String = "",
    val noticeTime: String = "",
    val noticeReason: String = ""
)

@HiltViewModel
class PreConsiderationDetailViewModel @Inject constructor(
    private val repository: PreConsiderationRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val preConsiderationId: Long = checkNotNull(savedStateHandle["preConsiderationId"]) {
        "PreConsiderationId is required in SavedStateHandle"
    }

    private val _uiState = MutableStateFlow(PreConsiderationDetailUiState())
    val uiState: StateFlow<PreConsiderationDetailUiState> = _uiState

    init {
        loadPostDetail()
    }

    fun loadPostDetail() {
        viewModelScope.launch {
            when (val result = repository.getPostDetail(preConsiderationId)) {
                is NetworkResult.Success -> {
                    val title = result.data.title
                    val content = result.data.content
                    val noticeDate = result.data.eventDate
                    val noticeTime = result.data.eventTime
                    val noticeReason = result.data.eventReason

                    _uiState.value = PreConsiderationDetailUiState(
                        title = title,
                        content = content,
                        noticeDate = noticeDate,
                        noticeTime = noticeTime,
                        noticeReason = noticeReason
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.value = PreConsiderationDetailUiState()
                }
            }
        }
    }
}