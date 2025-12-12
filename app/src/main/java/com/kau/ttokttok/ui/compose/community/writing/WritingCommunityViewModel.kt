package com.kau.ttokttok.ui.compose.community.writing

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.community.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

data class WritingCommunityUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface WritingCommunityEvent {
    data object Success : WritingCommunityEvent
    data class Error(val message: String) : WritingCommunityEvent
}

@HiltViewModel
class WritingCommunityViewModel @Inject constructor(
    private val useCase: CreatePostUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WritingCommunityUiState())
    val uiState: StateFlow<WritingCommunityUiState> = _uiState

    private val _event = MutableSharedFlow<WritingCommunityEvent>()
    val event: SharedFlow<WritingCommunityEvent> = _event.asSharedFlow()

    fun createPost(title: String, content: String, imageUri: Uri?) {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            useCase.invoke(
                title = title,
                content = content,
                imageUri = imageUri?.toString()
            )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )

                    emit(WritingCommunityEvent.Success)
                }

                .onFailure { e ->
                    val message = e.message ?: "게시글 작성에 실패했습니다."

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = message
                    )

                    emit(WritingCommunityEvent.Error(message))
                }

        }
    }

    private fun emit(event: WritingCommunityEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}