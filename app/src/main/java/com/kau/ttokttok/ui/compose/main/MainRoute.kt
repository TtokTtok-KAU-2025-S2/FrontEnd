package com.kau.ttokttok.ui.compose.main

import androidx.compose.runtime.Composable
import com.kau.ttokttok.ui.navigation.Destination

@Composable
fun MainRoute(
    // viewModel
    onNavigate: (Destination) -> Unit,
    onBack: () -> Unit = {}
) {

    MainScreen(
        onNavigate = onNavigate
    )
}