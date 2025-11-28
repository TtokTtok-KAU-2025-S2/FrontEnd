package com.kau.ttokttok.ui.compose.preconsideration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.local.repository.PreConsiderationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreConsiderationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val posts: List<PreConsiderationPost>? = null
)

@HiltViewModel
class PreConsiderationViewModel @Inject constructor(
    private val repository: PreConsiderationRepositoryImpl
): ViewModel() {
    private val _uiState = MutableStateFlow(PreConsiderationUiState())
    val uiState: StateFlow<PreConsiderationUiState> = _uiState

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            when (val result = repository.getPosts()) {
                is NetworkResult.Success -> {
                    val uiPosts = result.data.preNotices.map { dto ->
                        PreConsiderationPost(
                            id = dto.preNoticeId,
                            title = dto.title,
                            authorLocation = String.format("%s동", dto.authorDong)
                        )
                    }

                    _uiState.value = _uiState.value.copy(
                        posts = uiPosts
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        posts = emptyList()
                    )
                }
            }
        }
    }
}