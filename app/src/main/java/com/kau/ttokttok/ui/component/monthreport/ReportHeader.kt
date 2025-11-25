package com.kau.ttokttok.ui.component.monthreport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReportHeader(
    modifier: Modifier = Modifier,
    dateText: String
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2563EB), // from-blue-600
            Color(0xFF1D4ED8), // via-blue-700
            Color(0xFF1E3A8A)  // to-blue-900
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = gradientBrush,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                // 아이콘 원 (w-8 h-8 bg-white rounded-full)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "똑똑",
                        color = Color(0xFF2563EB), // text-blue-600
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = dateText,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                text = "똑똑 소음 분석 리포트",
                color = Color(0xFFDBEAFE), // text-blue-100
                fontSize = 13.sp
            )
        }
    }
}