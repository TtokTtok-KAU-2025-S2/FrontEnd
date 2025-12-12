package com.kau.ttokttok.ui.compose.community.writing

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun WritingCommunityRoute(
    viewModel: WritingCommunityViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect((Unit)) {
        viewModel.event.collect { event ->
            when (event) {
                is WritingCommunityEvent.Success -> {
                    onClickBack()
                }

                is WritingCommunityEvent.ShowAlert -> {
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

    WritingCommunityScreen(
        onClickCreate = {
            title, content, imageUri -> viewModel.createPost(title, content, imageUri)
        },
        onClickBack = onClickBack
    )
}