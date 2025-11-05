package com.kau.ttokttok.ui.compose.setting

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController

@Composable
fun SettingRoute(
    // viewModel
    onBack: () -> Unit = {}
) {
    SettingScreen(
        onBack = onBack
    )
}