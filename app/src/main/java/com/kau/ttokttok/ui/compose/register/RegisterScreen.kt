package com.kau.ttokttok.ui.compose.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.auth.*
import com.kau.ttokttok.ui.component.auth.button.RegisterButton
import com.kau.ttokttok.ui.component.common.background.StarField
@Preview
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onClickRegister: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
) {
    var aptId by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var buildingNumber by rememberSaveable { mutableStateOf("")}
    var unitNumber by rememberSaveable { mutableStateOf("")}

    var pwVisible by rememberSaveable { mutableStateOf(false)}

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
            .navigationBarsPadding(),
            contentAlignment = Alignment.Center
    ) {
        // 별빛 배경
        StarField(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.35f),
        )

        // 페이드 보카시
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(300f, 300f),
                        radius = 900f
                    )
                ),
        )

        FrostedPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
                .padding(horizontal = 24.dp)
        ) {
            // 로고/타이틀 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp) // w-16 h-16
                        .clip(RoundedCornerShape(16.dp)) // rounded-2xl
                        .background(Color.White.copy(alpha = 0.2f)) // bg-white/20
                        .padding(12.dp), // 내부 여백
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp) // w-8 h-8
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp) // w-4 h-4
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF2563EB)) // bg-blue-600
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "똑똑",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "아파트 소음 관리 서비스",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            AuthInputField(
                onValueChange = { aptId = it },
                value = aptId,
                headerText = "아파트 번호",
                imageVector = Icons.Outlined.AddHome,
                exampleText = "101"
            )

            Spacer(modifier.height(16.dp))

            AuthInputField(
                onValueChange = { email = it },
                value = email,
                headerText = "이메일",
                imageVector = Icons.Outlined.Email,
                exampleText = "example@gmail.com"
            )

            Spacer(Modifier.height(16.dp))

            PasswordField(
                value = password,
                onValueChange = { password = it }
            )

            Spacer(Modifier.height(16.dp))

            Row (
                modifier = Modifier,
                horizontalArrangement =  Arrangement.spacedBy(12.dp)
            ) {
                NoIconAuthInputField(
                    modifier = Modifier.weight(1f),
                    onValueChange = { buildingNumber = it },
                    value = buildingNumber,
                    headerText = "동",
                    exampleText = "000동"
                )

                NoIconAuthInputField(
                    modifier = Modifier.weight(1f),
                    onValueChange = { unitNumber = it},
                    value = unitNumber,
                    headerText = "호수",
                    exampleText = "000호"
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "동/호수 정보는 본인 확인용으로만 사용되며, 다른\n사용자에게 공개되지 않습니다.",
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(32.dp))

            RegisterButton(
                onClick = {
                    onClickRegister(aptId, email, password, buildingNumber, unitNumber)
                }
            )
        }
    }
}