package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PreConsiderationDetailRoute(
    viewModel: PreConsiderationDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onEdit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PreConsiderationDetailEvent.DeleteSuccess -> {
                    onClickBack()
                }
            }
        }
    }

    PreConsiderationDetailScreen(
        uiState = uiState,
        onClickBack = onClickBack,
        onEdit = onEdit,
        onDelete = { viewModel.deletePostDetail() }
    )
}