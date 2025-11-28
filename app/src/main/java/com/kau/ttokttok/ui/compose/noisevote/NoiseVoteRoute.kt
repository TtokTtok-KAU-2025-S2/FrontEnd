package com.kau.ttokttok.ui.compose.noisevote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NoiseVoteRoute(
    viewModel: NoiseVoteViewModel = hiltViewModel(),
    onClickPost: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoiseVoteScreen(
        uiState = uiState,
        onClickPost = onClickPost
    )
}