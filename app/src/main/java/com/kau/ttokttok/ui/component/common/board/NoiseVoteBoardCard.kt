package com.kau.ttokttok.ui.component.common.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* palette */
private val White    = Color.White
private val Gray900  = Color(0xFF111827)
private val Gray700  = Color(0xFF374151)
private val Gray500  = Color(0xFF6B7280)
private val Gray200  = Color(0xFFE5E7EB)
private val Blue600  = Color(0xFF2563EB)
private val Slate700 = Color(0xFF334155)

private val Slate800 = Color(0xFF1E293B)
private val Gray400 = Color(0xFF9CA3AF)
private val Gray100 = Color(0xFFF3F4F6)

@Composable
fun NoiseVoteBoardCard(
    modifier: Modifier = Modifier,
    authorLocation: String,
    title: String,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 상단 메타
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${authorLocation} 주민",
                    color = Gray400,
                    fontSize = 12.sp
                )
            }

            // 요약
            Text(
                text = title,
                color = Gray100,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
