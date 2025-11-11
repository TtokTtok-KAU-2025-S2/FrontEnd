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
        onVote = { voteCount ->
            // TODO: viewModel.vote(voteType) 같은 형태로 연결
        },
        onAddComment = { content ->
            // TODO: viewModel.addComment(content) 같은 형태로 연결
        }
    )
}
