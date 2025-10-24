package com.kau.ttokttok.ui.compose.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kau.ttokttok.ui.component.common.background.StarField
import com.kau.ttokttok.ui.component.common.card.GlassCard
import com.kau.ttokttok.ui.component.common.card.GlassCardClickable
import com.kau.ttokttok.ui.navigation.Destination

@Preview
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit = {}
) {
    // TODO: 디자인 디테일 한번 더 잡기

    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

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
        StarField(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.35f)
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
                .verticalScroll(rememberScrollState()),
        ) {
            HomeHeader(
                onClickNotification = { onNavigate(Destination.NOTIFICATION) },
                onClickSetting = { onNavigate(Destination.SETTING) },
            )

            Spacer(Modifier.height(32.dp))

            MonthlyNeighborCard()

            Spacer(Modifier.height(32.dp))

            // TODO: 섬 구현
            NavigatorPreview(
                onNavigate = { dest -> onNavigate(dest)}
            )

            Spacer(Modifier.height(32.dp))

            // 사전 양해 / 게시판
            QuickActionsGrid(
                onNavigate = { dest -> onNavigate(dest)},
                modifier = modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            // 나의 배려 현황
            MyCareStatusCard(
                onNavigate = { dest -> onNavigate(dest)}
            )
        }
    }
}

@Composable
fun HomeHeader(
    onClickNotification: () -> Unit,
    onClickSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val boxShadowAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .blur(0.2.dp)
            .border(1.dp, Color.White.copy(alpha = 0.1f))
            .zIndex(10f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 좌측: 아이콘 + 텍스트
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Animated Icon Container
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f))
                            .shadow(elevation = boxShadowAnimation.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "home",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Text
                    Column {
                        Text(
                            text = "똑똑",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            // TODO: Repository 연결 후 바꾸기
                            text = "101동 501호",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }

                // 우측: 버튼 2개
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onClickNotification,
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.1f))
                            .background(Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "notifications",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onClickSetting,
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.1f))
                            .background(Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyNeighborCard(
    modifier: Modifier = Modifier,
    month: String = "12월",
    timeOfDay: String = "day" // "day", "sunset", "night"
) {
    // 등장 애니메이션
    val enterTransition = remember { Animatable(20f) }
    val alpha = remember { Animatable(0f) }

    val offsetY = remember { Animatable(20f)}

    LaunchedEffect(Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
        )
        alpha.animateTo(1f, animationSpec = tween(800))
    }

    // 회전 아이콘 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10_000, easing = LinearEasing)
        )
    )

    // 트로피 흔들림 애니메이션
    val trophyRotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val trophyScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // 색상 테마
    val (badgeColor, badgeTextColor, ringColor, fallbackBgColor, fallbackBorderColor) = when (timeOfDay) {
        "day" -> listOf(Color(0xFF4CAF50).copy(alpha = 0.3f), Color(0xFF1B5E20), Color(0xFFB39DDB), Color(0xFF7E57C2), Color(0xFFB39DDB))
        "sunset" -> listOf(Color(0xFFFF9800).copy(alpha = 0.3f), Color(0xFFE65100), Color(0xFF9575CD), Color(0xFF7E57C2), Color(0xFF9575CD))
        else -> listOf(Color(0xFF2196F3).copy(alpha = 0.3f), Color(0xFF0D47A1), Color(0xFF7986CB), Color(0xFF3F51B5), Color(0xFF7986CB))
    }

    // 카드
    GlassCard(
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY.value
                this.alpha = alpha.value
            }
            .shadow(
                8.dp,
                RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.25f),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                1.dp,
                Color.White.copy(alpha = 0.2f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer { rotationZ = rotation }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "이달의 배려이웃",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Badge
                Box(
                    modifier = Modifier
                        .background(badgeColor, RoundedCornerShape(8.dp))
                        .border(1.dp, badgeColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = month, color = badgeTextColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Content
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, ringColor.copy(alpha = 0.6f), CircleShape)
                        .clip(CircleShape)
                        .background(fallbackBgColor.copy(alpha = 0.4f))
                        .border(1.dp, fallbackBorderColor.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "김배",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("김배려 님", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        "102동 304호 • 배려점수 1,480점",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }

                // Trophy 애니메이션
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = trophyRotation
                            scaleX = trophyScale
                            scaleY = trophyScale
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 32.sp)
                }
            }
        }
    }
}

@Composable
fun QuickActionsGrid(
    onNavigate: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1) 사전 양해
        ActionCardItem(
            title = "사전 양해",
            subtitle = "미리 알려드리기",
            delayMs = 0, // TODO: 테스트 이후 바꾸기
            gradient = listOf(Color(0xFFF472B6), Color(0xFFF43F5E)), // from-pink-400 to-rose-500
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            },
            onClick = { onNavigate(Destination.PRECONSIDERATION) },
            modifier = Modifier.weight(1f)
        )

        // 2) 게시판
        ActionCardItem(
            title = "게시판",
            subtitle = "이웃과 소통하기",
            delayMs = 0, // TODO: 테스트 이후 바꾸기
            gradient = listOf(Color(0xFF60A5FA), Color(0xFF22D3EE)), // from-blue-400 to-cyan-500
            icon = {
                Icon(
                    imageVector = Icons.Default.ChatBubble, // 적절히 교체 가능
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            },
            onClick = { onNavigate(Destination.COMMUNITY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ActionCardItem(
    title: String,
    subtitle: String,
    delayMs: Int,
    gradient: List<Color>,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 등장 애니메이션: y:40 -> 0, alpha:0 -> 1
    val offsetY = remember { Animatable(40f) }
    val fade = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        // delay만큼 지연 후 실행
        kotlinx.coroutines.delay(delayMs.toLong())
        offsetY.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
        fade.animateTo(1f, tween(800))
    }

    // 눌림(press) 스케일
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f, label = "press-scale")

    GlassCardClickable(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY.value
                alpha = fade.value
                scaleX = scale
                scaleY = scale
            }
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 아이콘 타일 (그라데이션 박스)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradient,
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .shadow(8.dp, RoundedCornerShape(12.dp), clip = false)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MyCareStatusCard(
    earnedBadgesCount: Int = 3,
    onNavigate: (Destination) -> Unit
) {
    // 등장 애니메이션
    val offsetY = remember { Animatable(50f) }
    val fade = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        offsetY.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
        fade.animateTo(1f, tween(800))
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = offsetY.value
                alpha = fade.value
            },
        padding = PaddingValues(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "나의 배려 현황",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            // 3개 항목 그리드
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CareStatusItem(
                    value = "95",
                    label = "신뢰지수",
                    delay = 0.0, // TODO: 테스트 이후 바꾸기
                    onClick = { onNavigate(Destination.TRUST_SCORE) },
                    modifier = Modifier.weight(1f)
                )

                CareStatusItem(
                    value = "1250",
                    label = "배려포인트",
                    delay = 0.0, // TODO: 테스트 이후 바꾸기
                    onClick = { onNavigate(Destination.CARE_POINT) },
                    modifier = Modifier.weight(1f)
                )

                CareStatusItem(
                    value = "${earnedBadgesCount}개 획득",
                    label = "배려 뱃지",
                    delay = 0.0, // TODO: 테스트 이후 바꾸기
                    isBadge = true,
                    onClick = { onNavigate(Destination.BADGE) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CareStatusItem(
    value: String,
    label: String,
    delay: Double,
    isBadge: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulse 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val press = remember { MutableInteractionSource() }
    val pressed by press.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        if (pressed) 0.95f else 1f, label = "press-scale"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable(interactionSource = press, indication = null, onClick = onClick )
            .graphicsLayer {
                scaleX = scale * pressScale
                scaleY = scale * pressScale
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isBadge) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(bottom = 4.dp)
                )
                Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            } else {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

data class FloatingIslandStep(
    val destination: Destination,
    val title: String,
    val screen: String
)

@Composable
fun NavigatorPreview(
    onNavigate: (Destination) -> Unit
) {
    val steps = listOf(
        FloatingIslandStep(Destination.STEP1, "소음 캘린더", "step1"),
        FloatingIslandStep(Destination.STEP2, "월간 소음 리포트", "step2"),
        FloatingIslandStep(Destination.STEP3, "똑똑 공동 탐색", "step3")
    )

    FloatingIslandNavigator(
        steps = steps,
        onNavigate = onNavigate
    )
}

@Composable
fun FloatingIslandNavigator(
    steps: List<FloatingIslandStep>,
    onNavigate: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        steps.forEach { step ->
            GlassCardClickable(
                onClick = { onNavigate(step.destination) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = step.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
