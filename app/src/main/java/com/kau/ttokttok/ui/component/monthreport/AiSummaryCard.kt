package com.kau.ttokttok.ui.component.monthreport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.theme.*

@Composable
fun AiSummaryCard(
    modifier: Modifier = Modifier,
    summaryText: String = "이번 달 우리 아파트의 소음 민원은 전월 대비 12% 감소했습니다." +
            " 주요 소음 발생 시간은 밤 10시~11시 사이이며, 발걸음 소리가 가장 빈번한 소음 유형으로 나타났습니다." +
            " 특히 주말 저녁 시간대의 소음이 평일보다 높은 경향을 보였습니다." +
            " 주민들의 적극적인 사전 양해 게시와 투표 참여로 전반적인 소음 인식이 개선되고 있습니다."
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Purple900.copy(alpha = 0.5f),
            Blue900.copy(alpha = 0.5f)
        ),
        start = Offset(0f, 0f),
        end = Offset.Infinite
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Purple700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .background(gradientBrush)
                .padding(16.dp)
        ) {
            Text(
                text = "🤖 AI 분석 요약",
                color = Purple300,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = summaryText,
                color = Gray300,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
    }
}