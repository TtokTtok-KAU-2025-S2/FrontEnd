package com.kau.ttokttok.ui.component.auth.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TempPasswordDialog(
    modifier: Modifier = Modifier,
    initialEmail: String = "",
    onConfirm: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var inputEmail by rememberSaveable { mutableStateOf(initialEmail) }
    var showEmailError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "임시 비밀번호 발급",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                Text(
                    text = "가입하신 이메일을 입력해주세요.\n임시 비밀번호를 메일로 보내드릴게요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = {
                        inputEmail = it
                        if (showEmailError) showEmailError = false
                    },
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth(),
                    label = { Text("이메일") },
                    placeholder = { Text("example@gmail.com") },
                    isError = showEmailError,
                    supportingText = {
                        if (showEmailError) {
                            Text(
                                text = "올바른 이메일 형식을 입력해주세요.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // 간단한 검증 (원하면 더 빡세게 해도 됨)
                    val isValid = inputEmail.isNotBlank() && "@" in inputEmail
                    if (!isValid) {
                        showEmailError = true
                    } else {
                        onConfirm(inputEmail)
                    }
                },
                enabled = inputEmail.isNotBlank()
            ) {
                Text("발급 요청")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
