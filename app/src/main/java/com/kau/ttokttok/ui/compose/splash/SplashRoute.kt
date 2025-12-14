package com.kau.ttokttok.ui.compose.splash

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onMain: () -> Unit
) {
    val activity = LocalContext.current as? Activity

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SplashEvent.MoveToLogin -> onLogin()
                SplashEvent.MoveToMain -> onMain()
                is SplashEvent.ShowAlert -> {
                    dialogTitle.value = event.title
                    dialogMessage.value = event.message
                    showDialog.value = true
                }
            }
        }
    }

    if (showDialog.value) {
        AppDialog(
            title = dialogTitle.value,
            message = dialogMessage.value,
            onDismiss = {
                showDialog.value = false
                activity?.finishAffinity()
            }
        )
    }

    SplashScreen(
        uiState = uiState
    )
}