package com.kau.ttokttok.ui.compose.noisevote.comment.modify

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NoiseVoteCommentModifyRoute(
    viewModel: NoiseVoteCommentModifyViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is NoiseVoteCommentModifyEvent.OnSuccess -> {
                    onClickBack()
                }
            }
        }
    }

    NoiseVoteCommentModifyScreen(
        uiState = uiState,
        onClickModify = { content -> viewModel.onClickModify(content) },
        onClickBack = onClickBack
    )
}