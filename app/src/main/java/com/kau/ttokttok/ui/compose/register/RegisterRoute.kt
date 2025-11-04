package com.kau.ttokttok.ui.compose.register

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.kau.ttokttok.ui.component.common.AppDialog

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel = hiltViewModel(),
    onSuccess: () -> Unit = { },
    onBack: () -> Unit = { },
) {
    val snackBar = remember { SnackbarHostState() }

    val showDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("")}
    val dialogMessage = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.ShowMessage -> {
                    snackBar.showSnackbar(event.message)
                }

                is RegisterEvent.ShowAlert -> {
                    dialogTitle.value = event.title
                    dialogMessage.value = event.message
                    showDialog.value = true
                }

                is RegisterEvent.NavigateHome -> {
                    onSuccess()
                }
            }
        }
    }

    if (showDialog.value) {
        AppDialog(
            title = dialogTitle.value,
            message = dialogMessage.value,
            onDismiss = { showDialog.value = false}
        )
    }

    RegisterScreen(
        onClickRegister = {
            email, password, buildingNumber, unitNumber ->
            viewModel.onClickRegister(email, password, buildingNumber,unitNumber)
        },

        onBack = {
            onBack
        }
    )
}