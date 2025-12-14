package com.kau.ttokttok.ui.compose.main

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.navigation.Destination

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigate: (Destination) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.getUserInformation()
    }

    MainScreen(
        uiState = uiState,
        onNavigate = onNavigate
    )
}