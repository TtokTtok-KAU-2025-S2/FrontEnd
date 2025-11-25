package com.kau.ttokttok.ui.component.monthreport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.theme.*
import kotlin.math.max

private val DefaultColors = listOf(
    Color(0xFF60A5FA), // blue-400
    Color(0xFFF87171), // red-400
    Color(0xFF34D399), // emerald-400
    Color(0xFFFBBF24), // amber-400
    Color(0xFFA78BFA), // violet-400
    Color(0xFF22D3EE), // cyan-400
)

data class NoiseTypeSlice(
    val type: String,
    val count: Int,
    val color: Color
)
private fun noiseTypeSlicesOf(pairs: List<Pair<String, Int>>): List<NoiseTypeSlice> =
    pairs.mapIndexed { idx, (t, c) ->
        NoiseTypeSlice(
            type = t,
            count = c,
            color = DefaultColors[idx % DefaultColors.size]
        )
    }

// TODO: 카테고리 Enum 및 연결하기!

@Composable
fun NoiseTypesCard(
    modifier: Modifier = Modifier,
    categoryStats: Map<String, Int> = mapOf(
        "아이들 뛰는 소리" to 12,
        "가구 끄는 소리" to 8,
        "악기/노래" to 5,
        "반려동물" to 3,
        "가전소음" to 2
    )
) {
    val slices: List<NoiseTypeSlice> = noiseTypeSlicesOf(
        categoryStats.entries
            .sortedByDescending { it.value }
            .map { it.key to it.value }
    )

    val total = max(1, slices.sumOf { it.count })

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "소음 유형 분포",
                color = Gray300,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 파이 차트
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.size(180.dp)
                ) {
                    val diameter = size.minDimension
                    val pieSize = Size(diameter, diameter)

                    var startAngle = -90f // 위쪽(12시)부터
                    slices.forEach { slice ->
                        val sweep = (slice.count.toFloat() / total.toFloat()) * 360f
                        drawArc(
                            color = slice.color,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = true,
                            size = pieSize
                        )
                        startAngle += sweep
                    }
                }
            }

            // 범례 (최대 4개 표시)
            Spacer(Modifier.height(12.dp))
            val legendItems = slices.take(4)
            legendItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { s ->
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(s.color, CircleShape)
                            )
                            Text(
                                text = "${s.type} (${s.count}건)",
                                color = Gray400,
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
