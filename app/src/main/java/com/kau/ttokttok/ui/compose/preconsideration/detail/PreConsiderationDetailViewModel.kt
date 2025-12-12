package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.usecase.preconsideration.DeletePostPreConsiderationUseCase
import com.kau.ttokttok.domain.usecase.preconsideration.LoadPostDetailPreConsiderationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreConsiderationDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val preConsiderationBoardDetail: PreConsiderationBoardDetail? = null
)

sealed class PreConsiderationDetailEvent {
    data object DeleteSuccess : PreConsiderationDetailEvent()

    data class ShowAlert(val title: String, val message: String) : PreConsiderationDetailEvent()
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

    private val _events = MutableSharedFlow<PreConsiderationDetailEvent>()
    val events: SharedFlow<PreConsiderationDetailEvent> = _events

    init {
        loadPostDetail()
    }

    fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            loadPostDetailUseCase.invoke(preConsiderationId)
                .onSuccess { response ->
                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = null,
                            preConsiderationBoardDetail = response
                        )
                    }
                }

                .onFailure { e ->
                    val errorMessage = e.message

                    _uiState.update { after ->
                        after.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }

                    emit(PreConsiderationDetailEvent.ShowAlert(
                        title = "실패",
                        message = errorMessage ?: "알 수 없는 오류입니다."
                    ))
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

                    _events.emit(PreConsiderationDetailEvent.DeleteSuccess)
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

    private fun emit(event: PreConsiderationDetailEvent) {
        _events.tryEmit(event)
    }
}