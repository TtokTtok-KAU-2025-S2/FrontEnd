package com.kau.ttokttok.ui.compose.community.detail

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun CommunityDetailRoute(
    viewModel: CommunityDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CommunityDetailEvent.ShowAlert -> {
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

    CommunityDetailScreen(
        uiState = uiState,
        onClickBack = onClickBack
    )
}