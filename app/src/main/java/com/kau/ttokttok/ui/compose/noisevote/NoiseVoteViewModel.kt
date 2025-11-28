package com.kau.ttokttok.ui.compose.noisevote

import android.util.Log
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.usecase.noisevote.LoadPostsNoiseVoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoiseVoteUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val posts: List<NoiseVoteBoard> = emptyList()
)

@HiltViewModel
class NoiseVoteViewModel @Inject constructor(
    private val useCase: LoadPostsNoiseVoteUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(NoiseVoteUiState())
    val uiState: StateFlow<NoiseVoteUiState> = _uiState

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

            useCase.invoke()
                .onSuccess { posts ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            posts = posts
                        )
                    }
                }

                .onFailure { e ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }

                    // TODO: 다이얼로그 추가
                }
        }
    }
}