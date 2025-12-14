package com.kau.ttokttok.ui.compose.preconsideration.writing

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun WritingPreConsiderationRoute(
    viewModel: WritingPreConsiderationViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is WritingPreconsiderationEvent.ShowAlert -> {
                    dialogTitle.value = event.title
                    dialogMessage.value = event.message
                    showDialog.value = true
                }

                WritingPreconsiderationEvent.Success -> {
                    onClickBack()
                }
            }
        }
    }

    if (showDialog.value) {
        AppDialog(
            title = dialogTitle.value,
            message = dialogMessage.value,
            onDismiss = { showDialog.value = false }
        )
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