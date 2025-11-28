package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.local.repository.PreConsiderationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreConsiderationDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val title: String = "",
    val content: String = "",
    val buildingNumber: Int = 0,
    val unitNumber: Int = 0,
    val noticeDate: String = "",
    val noticeTime: String = "",
    val noticeReason: String = "",
    val createdAt: String = ""
)

@HiltViewModel
class PreConsiderationDetailViewModel @Inject constructor(
    private val repository: PreConsiderationRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val preConsiderationId: Long = checkNotNull(savedStateHandle.get<Long>("preConsiderationId"))

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
                    val buildingNumber = result.data.authorDong
                    val noticeDate = result.data.eventDate
                    val noticeTime = result.data.eventTime
                    val noticeReason = result.data.eventReason
                    val createdAt = result.data.createdAt

                    _uiState.value = PreConsiderationDetailUiState(
                        title = title,
                        content = content,
                        buildingNumber = buildingNumber,
                        noticeDate = noticeDate,
                        noticeTime = noticeTime,
                        noticeReason = noticeReason,
                        createdAt = createdAt
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.value = PreConsiderationDetailUiState()
                }
            }
        }
    }
}