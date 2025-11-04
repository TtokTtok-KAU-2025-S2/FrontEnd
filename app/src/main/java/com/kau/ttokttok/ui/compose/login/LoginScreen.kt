package com.kau.ttokttok.ui.compose.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.component.common.background.StarField
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    // 디자인만: 외부로 노출되는 콜백만 둔다 (VM 없음)
    onClickLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onClickRegister: () -> Unit = {}
) {
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    // 화면 상태 (디자인/미리보기용)
    var email by rememberSaveable { mutableStateOf("") }
    var pw by rememberSaveable { mutableStateOf("") }
    var pwVisible by rememberSaveable { mutableStateOf(false) }
    var showCard by remember { mutableStateOf(false) }

    // 간단한 등장 애니메이션
    LaunchedEffect(Unit) { showCard = true }

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
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                    value = pw,
                    onValueChange = { pw= it }
                )

                Spacer(Modifier.height(32.dp))

                LoginButton(
                    onClick = {
                        onClickLogin(email, pw)
                    }
                )

                Spacer(Modifier.height(32.dp))

                SignupButton(
                    onClick = onClickRegister
                )
            }
        }
    }
}

@Composable
fun FrostedPanel(
    modifier: Modifier = Modifier,
    // rounded-3xl ≈ 24.dp
    corner: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val panelColor = Color.White.copy(alpha = 0.10f)   // bg-white/10
    val borderColor = Color.White.copy(alpha = 0.20f)  // border-white/20

    // shadow-2xl 느낌 → shadowElevation 크게
    Surface(
        shape = corner,
        color = panelColor,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .clip(corner)
    ) {
        Column(Modifier.padding(24.dp)) { // p-8
            content()
        }
    }
}

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "이메일",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "example@gmail.com",
                    color = Color.White.copy(alpha = 0.6f) // placeholder:text-white/60
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f), // 아이콘 색
                    modifier = Modifier.size(18.dp)
                )
            },
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
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "비밀번호",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "비밀번호를 입력하세요",
                    color = Color.White.copy(alpha = 0.6f) // placeholder:text-white/60
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Outlined.VisibilityOff
                        else
                            Icons.Outlined.Visibility,
                        contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보이기",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)), // rounded-xl
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),   // bg-white/10
                unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
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
fun LoginButton(
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
            "로그인",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SignupButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), // h-12
        shape = RoundedCornerShape(12.dp), // rounded-xl
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,   // bg-transparent
            contentColor = Color.White            // text-white
        )
    ) {
        Text("회원가입 하기")
    }
}