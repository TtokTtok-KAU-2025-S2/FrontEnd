package com.kau.ttokttok.ui.compose.preconsideration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PreConsiderationRoute(
    viewModel: PreConsiderationViewModel = hiltViewModel(),
    onClickCreatePost: () -> Unit,
    onClickPost: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PreConsiderationScreen(
        uiState = uiState,
        onClickCreatePost = onClickCreatePost,
        onClickPost = onClickPost,
        onRefresh = {
            viewModel.loadPosts()
        }
    )
}