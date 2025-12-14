package com.kau.ttokttok.ui.compose.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.auth.AuthInputField
import com.kau.ttokttok.ui.component.auth.FrostedPanel
import com.kau.ttokttok.ui.component.auth.PasswordField
import com.kau.ttokttok.ui.component.auth.button.LoginButton
import com.kau.ttokttok.ui.component.auth.button.SignupButton
import com.kau.ttokttok.ui.component.auth.dialog.TempPasswordDialog
import com.kau.ttokttok.ui.component.common.background.StarField

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onClickLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onClickRegister: () -> Unit = {},
    onRequestTempPassword: (String) -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showTempPasswordDialog by rememberSaveable { mutableStateOf(false)}

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A237E), // deep indigo
                        Color(0xFF0D47A1)  // deep blue
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        StarField(
            modifier = modifier
                .fillMaxSize()
                .alpha(0.35f),
        )

        // 페이드 보카시
        Box(
            modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(300f, 300f),
                        radius = 900f
                    )
                )
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FrostedPanel(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // 로고/타이틀 영역
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 애니메이션 들어가는 로고
                    Box(
                        modifier = modifier
                            .size(64.dp) // w-16 h-16
                            .clip(RoundedCornerShape(16.dp)) // rounded-2xl
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(12.dp), // 내부 여백
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = modifier
                                .size(32.dp) // w-8 h-8
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = modifier
                                    .size(16.dp) // w-4 h-4
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFF2563EB))
                            )
                        }
                    }

                    Spacer(modifier.height(16.dp))

                    Text(
                        text = "똑똑",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "아파트 소음 관리 서비스",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }

                AuthInputField(
                    onValueChange = { email = it },
                    value = email,
                    headerText = "이메일",
                    imageVector = Icons.Outlined.Email,
                    exampleText = "example@gmail.com"
                )

                Spacer(modifier.height(16.dp))

                PasswordField(
                    value = password,
                    onValueChange = { password= it }
                )

                Spacer(modifier.height(32.dp))

                LoginButton(
                    onClick = {
                        onClickLogin(email, password)
                    }
                )

                Spacer(modifier.height(32.dp))

                SignupButton(
                    onClick = onClickRegister
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "비밀번호를 잊으셨나요?",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 4.dp)
                )

                TextButton(
                    onClick = { showTempPasswordDialog = true },
                    modifier = Modifier.wrapContentWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF64B5F6)
                    )
                ) {
                    Text(
                        text = "임시 비밀번호 발급하기",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showTempPasswordDialog) {
        TempPasswordDialog(
            initialEmail = email,
            onConfirm = { inputEmail ->
                onRequestTempPassword(inputEmail)
                showTempPasswordDialog = false
            },
            onDismiss = {
                showTempPasswordDialog = false
            }
        )
    }
}