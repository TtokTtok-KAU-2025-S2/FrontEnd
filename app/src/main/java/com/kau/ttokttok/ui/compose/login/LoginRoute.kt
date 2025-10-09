package com.kau.ttokttok.ui.compose.login

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel()
    // TODO: Navigator 추가하기
) {
    // val uiState by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.ShowMessage -> {
                    snackbar.showSnackbar(event.message)
                }

                LoginEvent.NavigateHome -> {
                    // TODO: Navigator 추가하기
                }

                LoginEvent.NavigateSignup -> {
                    // TODO: Navigator 추가하기
                }
            }
        }
    }

    // 콜백 연결
    LoginScreen(
        onClickLogin = {
            email, pw -> viewModel.onClickLogin(email, pw)
        },

        onClickSignup = {
            viewModel.onClickSignUp()
        },

        onClickKaKao = {
            viewModel.onClickKaKao()
        },

        onClickNaver = {
            viewModel.onClickNaver()
        },

        onClickFindId = {
            viewModel.onClickFindId()
        },

        onClickFindPassword = {
            viewModel.onClickFindPassword()
        }
    )
}