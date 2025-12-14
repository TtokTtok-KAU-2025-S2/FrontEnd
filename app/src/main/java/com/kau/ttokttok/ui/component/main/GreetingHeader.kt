package com.kau.ttokttok.ui.component.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GreetingHeader(
    modifier: Modifier = Modifier,
    greeting: String = "하루",
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
            modifier = modifier
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