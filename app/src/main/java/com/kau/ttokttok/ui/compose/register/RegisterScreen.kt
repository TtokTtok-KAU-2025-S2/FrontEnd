package com.kau.ttokttok.ui.compose.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.kau.ttokttok.ui.component.common.background.StarField
import com.kau.ttokttok.ui.compose.login.EmailField
import com.kau.ttokttok.ui.compose.login.FrostedPanel
import com.kau.ttokttok.ui.compose.login.PasswordField

@Preview
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onClickRegister: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onBack: () -> Unit = { }
) {
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
            ),
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
                // 애니메이션 들어가는 로고
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

            EmailField(
                value = email,
                onValueChange = { email = it }
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
                BuildingNumberField(
                    value = buildingNumber,
                    onValueChange = { buildingNumber = it },
                    modifier = Modifier.weight(1f)
                )

                UnitNumberField(
                    value = unitNumber,
                    onValueChange = { unitNumber = it },
                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                "동/호수 정보는 본인 확인용으로만 사용되며, 다른\n사용자에게 공개되지 않습니다.",
                color = Color.White
            )

            Spacer(Modifier.height(32.dp))

            RegisterButton(
                onClick = {
                    onClickRegister(email, password, buildingNumber, unitNumber)
                }
            )
        }
    }
}

@Composable
fun BuildingNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "동",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "000동",
                    color = Color.White.copy(alpha = 0.6f) // placeholder:text-white/60
                )
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.White), // 입력 텍스트 색
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)), // rounded-xl
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),   // bg-white/10
                unfocusedContainerColor = Color.White.copy(alpha = 0.1f), // bg-white/10
                disabledContainerColor = Color.White.copy(alpha = 0.1f),

                focusedIndicatorColor = Color.White.copy(alpha = 0.4f),   // focus:border-white/40
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f), // border-white/20

                cursorColor = Color.White,
                focusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                focusedTrailingIconColor = Color.White.copy(alpha = 0.6f),
                unfocusedTrailingIconColor = Color.White.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun UnitNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "호수",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "000호",
                    color = Color.White.copy(alpha = 0.6f) // placeholder:text-white/60
                )
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.White), // 입력 텍스트 색
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)), // rounded-xl
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),   // bg-white/10
                unfocusedContainerColor = Color.White.copy(alpha = 0.1f), // bg-white/10
                disabledContainerColor = Color.White.copy(alpha = 0.1f),

                focusedIndicatorColor = Color.White.copy(alpha = 0.4f),   // focus:border-white/40
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f), // border-white/20

                cursorColor = Color.White,
                focusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                focusedTrailingIconColor = Color.White.copy(alpha = 0.6f),
                unfocusedTrailingIconColor = Color.White.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun RegisterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), // h-12
        shape = RoundedCornerShape(12.dp), // rounded-xl
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.2f),   // bg-white/20
            contentColor = Color.White                        // text-white
        ),
        contentPadding = PaddingValues(0.dp) // Tailwind 기본처럼 꽉 채운 텍스트
    ) {
        Text(
            "회원가입",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}