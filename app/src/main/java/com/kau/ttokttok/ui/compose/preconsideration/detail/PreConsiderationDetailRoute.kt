package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PreConsiderationDetailRoute(
    viewModel: PreConsiderationDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PreConsiderationDetailScreen(
        uiState = uiState,
        onClickBack = onClickBack
    )
}