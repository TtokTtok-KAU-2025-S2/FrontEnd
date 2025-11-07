package com.kau.ttokttok.ui.compose.setting

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.kau.ttokttok.domain.model.User

@Composable
fun SettingRoute(
    // viewModel
    onBack: () -> Unit = {}
) {

    SettingScreen(
        user = User("", "101", "501"),
        onBack = onBack
    )
}