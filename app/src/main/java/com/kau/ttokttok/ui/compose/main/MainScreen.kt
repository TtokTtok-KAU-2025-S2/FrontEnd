package com.kau.ttokttok.ui.compose.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kau.ttokttok.R
import com.kau.ttokttok.ui.component.common.background.StarField
import com.kau.ttokttok.ui.component.common.card.GlassCardClickable
import com.kau.ttokttok.ui.navigation.Destination

@Preview
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit = {}
) {
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

            GreetingHeader()

            Spacer(Modifier.height(32.dp))

            MainSection(
                onButtonClick = { dest -> onNavigate(dest) }
            )

            Spacer(Modifier.height(32.dp))

            // 사전 양해 / 게시판
            QuickActionsGrid(
                onNavigate = { dest -> onNavigate(dest)},
                modifier = modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun HomeHeader(
    onClickNotification: () -> Unit,
    onClickSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    IconButton(
                        onClick = onClickNotification,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.25f))
                            .background(Color.White.copy(alpha = 0.1f))
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
                            .clip(CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.25f))
                            .background(Color.White.copy(alpha = 0.1f))
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
fun QuickActionsGrid(
    onNavigate: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
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
fun GreetingHeader(
    greeting: String = "하루",
    modifier: Modifier = Modifier,
    titleColor: Color = Color.White,
    subtitleColor: Color = Color(0xFFE9D5FF), // Tailwind purple-200
    badgeBackground: Color = Color.White.copy(alpha = 0.20f),
    badgeBorder: Color = Color.White.copy(alpha = 0.15f),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp), // mb-6
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp) // gap-4
    ) {
        // Badge (w-16 h-16 rounded-full bg-white/20)
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(badgeBackground)
                .border(width = 1.dp, color = badgeBorder, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "👋", fontSize = 28.sp) // text-3xl
        }

        Column {
            Text(
                text = "배려하는 이웃님,",
                color = titleColor,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp)) // mb-1 느낌
            Text(
                text = "좋은 $greeting 입니다!",
                color = titleColor,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "평화로운 이웃과 함께하는 하루",
                color = subtitleColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp) // mt-1
            )
        }
    }
}

@Composable
fun MainSection(
    mainSlides: List<MainSlide> = listOf(
        MainSlide(
            image = "",
            title = "소음 일기",
            description = "캘린더에서 날짜를 선택하여\n 소음을 기록하세요",
            buttonText = "일기 보기",
            destination = Destination.NOISE_LOG
        ),

        MainSlide(
            image = "",
            title = "소음 현황판",
            description = "이웃의 소음 리포트에\n 투표로 참여하세요",
            buttonText = "현황 보기",
            destination = Destination.NOISE_VOTE
        ),

        MainSlide(
            image = "",
            title = "월간 리포트",
            description = "AI가 분석한 우리 아파트의\n 소음 트렌드를 확인하세요",
            buttonText = "리포트 보기",
            destination = Destination.MONTH_REPORT
        )
    ),
    modifier: Modifier = Modifier,
    onButtonClick: (Destination) -> Unit = {}
) {
    var currentSlide by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp) // px-6
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp) // space-y-6
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)) // rounded-3xl
                    .background(Color.White.copy(alpha = 0.10f)) // bg-white/10
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.10f))
                ) {
                    // ============================
                    // 🔹 이미지 자리: 현재는 "검정 화면"으로 고정
                    //    나중에 이미지가 필요하면 아래 Box를 Image/AsyncImage로 교체
                    //    예)
                    //    Image(
                    //      painter = painterResource(id = R.drawable.your_image),
                    //      contentDescription = mainSlides[currentSlide].title,
                    //      modifier = Modifier.fillMaxSize(),
                    //      contentScale = ContentScale.Crop
                    //    )
                    // ============================
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp) // h-48
                            .background(Color.Black)
                    ) {
                        // Prev 버튼
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterStart)
                                .offset(x = 8.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .clickable {
                                    currentSlide =
                                        if (currentSlide == 0) mainSlides.size - 1 else currentSlide - 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_left),
                                contentDescription = "Prev",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Next 버튼
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterEnd)
                                .offset(x = (-8).dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .clickable {
                                    currentSlide =
                                        if (currentSlide == mainSlides.size - 1) 0 else currentSlide + 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 텍스트 + 버튼
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = mainSlides[currentSlide].title,
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = mainSlides[currentSlide].description,
                            color = Color(0xFFE9D5FF),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Button(
                            onClick = { onButtonClick(mainSlides[currentSlide].destination) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_volume),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(text = mainSlides[currentSlide].buttonText)
                        }
                    }

                    // 인디케이터
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        mainSlides.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width(if (index == currentSlide) 24.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == currentSlide)
                                            Color.White
                                        else
                                            Color.White.copy(alpha = 0.4f)
                                    )
                                    .padding(horizontal = 2.dp)
                                    .clickable { currentSlide = index }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }
}

// 데이터 모델
data class MainSlide(
    val image: String,          // 현재는 사용하지 않지만, 추후 이미지 복원 시 활용 가능
    val title: String,
    val description: String,
    val buttonText: String,
    val destination: Destination
)

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