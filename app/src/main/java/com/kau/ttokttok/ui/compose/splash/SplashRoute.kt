package com.kau.ttokttok.ui.compose.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SplashEvent.MoveToLogin -> onLogin()
                SplashEvent.MoveToMain -> onMain()
                is SplashEvent.ShowAlert -> TODO()
            }
        }
    }

    SplashScreen()
}