package com.kau.ttokttok.ui.compose.login

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onRegister: () -> Unit,
    onSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false)}
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.ShowAlert -> {
                    dialogTitle.value = event.title
                    dialogMessage.value = event.message
                    showDialog.value = true
                }

                LoginEvent.NavigateHome -> {
                    onSuccess()
                }
            }
        }
    }

    if (showDialog.value) {
        AppDialog(
            title = dialogTitle.value,
            message = dialogMessage.value,
            onDismiss = { showDialog.value = false }
        )
    }

    LoginScreen(
        onClickLogin = {
            email, pw -> viewModel.onClickLogin(email, pw)
        },

        onClickRegister = onRegister,

        onRequestTempPassword = {
            email -> viewModel.onRequestTempPassword(email)
        }
    )
}