package com.kau.ttokttok.ui.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.background.StarField
import com.kau.ttokttok.ui.component.main.GreetingHeader
import com.kau.ttokttok.ui.component.main.HomeHeader
import com.kau.ttokttok.ui.component.main.MainSection
import com.kau.ttokttok.ui.component.main.QuickActionsGrid
import com.kau.ttokttok.ui.navigation.Destination

@Preview
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState = MainUiState(),
    onNavigate: (Destination) -> Unit = {},
) {
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
    ) {
        StarField(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.35f)
        )

        // 페이드 보카시
        Box(
            Modifier
                .fillMaxSize()
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
                buildingNumber = uiState.buildingNumber,
                unitNumber = uiState.unitNumber
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