package com.kau.ttokttok.ui.compose.community.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CommunityDetailRoute(
    viewModel: CommunityDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityDetailScreen(
        uiState = uiState,
        onClickBack = onClickBack
    )
}