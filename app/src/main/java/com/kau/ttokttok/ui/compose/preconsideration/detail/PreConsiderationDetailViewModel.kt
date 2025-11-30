package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.usecase.preconsideration.DeletePostPreConsiderationUseCase
import com.kau.ttokttok.domain.usecase.preconsideration.LoadPostDetailPreConsiderationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreConsiderationDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val preConsiderationBoardDetail: PreConsiderationBoardDetail? = null
)

sealed class PreConsiderationDetailEvent {
    object DeleteSuccess : PreConsiderationDetailEvent()
}

@HiltViewModel
class PreConsiderationDetailViewModel @Inject constructor(
    private val loadPostDetailUseCase: LoadPostDetailPreConsiderationUseCase,
    private val deletePostUseCase: DeletePostPreConsiderationUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val preConsiderationId: Long = checkNotNull(savedStateHandle.get<Long>("preConsiderationId"))

    private val _uiState = MutableStateFlow(PreConsiderationDetailUiState())
    val uiState: StateFlow<PreConsiderationDetailUiState> = _uiState

    private val _event = MutableSharedFlow<PreConsiderationDetailEvent>()
    val event: SharedFlow<PreConsiderationDetailEvent> = _event

    init {
        loadPostDetail()
    }

    fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            loadPostDetailUseCase.invoke(preConsiderationId)
                .onSuccess { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            preConsiderationBoardDetail = response
                        )
                    }
                }

                .onFailure { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )
                    }

                    // TODO: 다이얼로그 추가하기
                }
        }
    }

    fun deletePostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true
                )
            }

            deletePostUseCase.invoke(preConsiderationId)
                .onSuccess { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = response
                        )
                    }

                    _event.emit(PreConsiderationDetailEvent.DeleteSuccess)
                }

                .onFailure { error ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }
}