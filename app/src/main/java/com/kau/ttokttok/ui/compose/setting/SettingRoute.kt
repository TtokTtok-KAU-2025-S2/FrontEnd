package com.kau.ttokttok.ui.compose.setting

import androidx.compose.runtime.Composable
import com.kau.ttokttok.domain.model.User

@Composable
fun SettingRoute(
    // viewModel
    onBack: () -> Unit = {}
) {
    SettingScreen(
        user = User("", 101, 101),
        onBack = onBack
    )
}