package com.kau.ttokttok.ui.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.navigation.Destination

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
            title = "공지사항",
            subtitle = "이웃과 소통하기",
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