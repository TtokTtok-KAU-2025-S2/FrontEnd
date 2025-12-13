package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun NoiseVoteDetailRoute(
    viewModel: NoiseVoteDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickCommentModify: (Long, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is NoiseVoteDetailEvent.ShowAlert -> {
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

    NoiseVoteDetailScreen(
        uiState = uiState,
        onRefresh = {
            viewModel.loadPostDetail()
        },
        onClickBack = onClickBack,
        onVoteClick = { voteType ->
            viewModel.clickVote(voteType)
        },
        onAddComment = { content ->
            viewModel.addComment(content)
        },
        onEditComment = { comment ->
            onClickCommentModify(comment.id, comment.content)
        },
        onDeleteComment = { comment ->
            viewModel.deleteComment(comment)
        }
    )
}
