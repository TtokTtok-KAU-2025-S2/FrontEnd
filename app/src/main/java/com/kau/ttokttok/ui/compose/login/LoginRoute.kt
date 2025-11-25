package com.kau.ttokttok.ui.compose.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    // 다이얼로그 상태
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

                LoginEvent.NavigateSignup -> {
                    onRegister()
                }
            }
        }
    }

    // 화면 다이얼로그 표시
    if (showDialog.value) {
        AppDialog(
            title = dialogTitle.value,
            message = dialogMessage.value,
            onDismiss = { showDialog.value = false }
        )
    }

    // 콜백 연결
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