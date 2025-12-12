package com.kau.ttokttok.ui.compose.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.usecase.community.LoadPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommunityUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val posts: List<CommunityBoard> = emptyList()
)

sealed interface CommunityEvent {
    data class ShowAlert(val title: String, val message: String): CommunityEvent
}

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState

    private val _events = MutableSharedFlow<CommunityEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<CommunityEvent> = _events.asSharedFlow()

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
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            posts = posts
                        )
                    }
                }

                .onFailure { e ->
                    val errorMessage = e.message ?: "ERROR"

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(
                        CommunityEvent.ShowAlert(
                            title = "글 조회 실패",
                            message = errorMessage
                        )
                    )
                }
        }
    }

    private fun emit(event: CommunityEvent) {
        _events.tryEmit(event)
    }
}