package com.kau.ttokttok.ui.compose.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.usecase.community.LoadPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommunityUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val posts: List<CommunityBoard> = emptyList(),
)

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            loadPostsUseCase.invoke()
                .onSuccess { posts ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            posts = posts
                        )
                    }
                }

                .onFailure {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    // TODO: 다이얼로그 추가
                }
        }
    }
}