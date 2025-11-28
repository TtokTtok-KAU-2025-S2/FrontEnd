package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NoiseVoteDetailRoute(
    viewModel: NoiseVoteDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoiseVoteDetailScreen(
        uiState = uiState,
        onClickBack = onClickBack,
        onVoteClick = { voteType ->
            viewModel.clickVote(voteType)
        },
        onAddComment = { content ->
            viewModel.addComment(content)
        },
        onEditComment = { comment ->
            viewModel.modifyComment(comment)
        },
        onDeleteComment = { comment ->
            viewModel.deleteComment(comment)
        }
    )
}
