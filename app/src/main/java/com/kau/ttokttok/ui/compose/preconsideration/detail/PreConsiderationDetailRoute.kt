package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun PreConsiderationDetailRoute(
    viewModel: PreConsiderationDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PreConsiderationDetailEvent.DeleteSuccess -> {
                    onClickBack()
                }

                is PreConsiderationDetailEvent.ShowAlert -> {
                    dialogTitle.value = event.title
                    dialogMessage.value = event.message
                    showDialog.value = true
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

    PreConsiderationDetailScreen(
        uiState = uiState,
        onRefresh = { viewModel.loadPostDetail() },
        onClickBack = onClickBack,
        onEdit = { onEdit(viewModel.preConsiderationId) },
        onDelete = { viewModel.deletePostDetail() }
    )
}