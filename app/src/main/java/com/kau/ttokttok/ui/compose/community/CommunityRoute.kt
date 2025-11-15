package com.kau.ttokttok.ui.compose.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CommunityRoute(
    viewModel: CommunityViewModel = hiltViewModel(),
    onClickCreatePost: () -> Unit,
    onClickPost: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityScreen(
        uiState = uiState,
        onClickCreatePost = onClickCreatePost,
        onClickPost = onClickPost,
        onRefresh = {
            viewModel.loadPosts()
        }
    )
}