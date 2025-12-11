package com.kau.ttokttok.ui.compose.preconsideration.writing

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WritingPreConsiderationRoute(
    viewModel: WritingPreConsiderationViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBar = remember { SnackbarHostState() }

    // 다이얼로그 상태
    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is WritingPreconsiderationEvent.ShowMessage -> {
                    snackBar.showSnackbar(event.message)
                }

                is WritingPreconsiderationEvent.ShowAlert -> {
                    dialogTitle.value = event.title
                    dialogMessage.value = event.message
                    showDialog.value = true
                }

                WritingPreconsiderationEvent.onSuccess -> {
                    onClickBack()
                }
            }
        }
    }

    WritingPreConsiderationScreen(
        uiState = uiState,
        isEdit = viewModel.isEdit,
        onClickBack = onClickBack,
        onClickCreate = {
            title, content, noticeDate, noticeTime, noticeReason
                -> viewModel.onClickCreate(title, content, noticeDate, noticeTime, noticeReason)
        },
        onClickModify = {
            title, content, noticeDate, noticeTime, noticeReason
                -> viewModel.onClickModify(title, content, noticeDate, noticeTime, noticeReason)
        }
    )
}