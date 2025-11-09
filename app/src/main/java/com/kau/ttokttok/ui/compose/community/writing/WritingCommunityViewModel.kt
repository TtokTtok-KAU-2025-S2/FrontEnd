package com.kau.ttokttok.ui.compose.community.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.usecase.CommunityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WritingCommunityUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class WritingCommunityViewModel @Inject constructor(
    // private val communityUseCase: CommunityUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WritingCommunityUiState())
    val uiState: StateFlow<WritingCommunityUiState> = _uiState

    fun createPost(title: String, content: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // communityUseCase.createPost(title = title, content = content)
        }
    }
}