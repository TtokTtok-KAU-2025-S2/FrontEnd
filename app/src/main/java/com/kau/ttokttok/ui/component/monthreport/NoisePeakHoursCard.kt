package com.kau.ttokttok.ui.component.monthreport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.theme.*

@Composable
fun NoisePeakHoursCard(
    modifier: Modifier = Modifier,
    hourlyStats: Map<String, Int> = mapOf(
        "0" to 1,
        "1" to 0,
        "2" to 0,
        "3" to 1,
        "4" to 2,
        "5" to 3,
        "6" to 4,
        "7" to 6,
        "8" to 8,
        "9" to 10,
        "10" to 12,
        "11" to 9,
        "12" to 6,
        "13" to 5,
        "14" to 7,
        "15" to 11,
        "16" to 9,
        "17" to 6,
        "18" to 7,
        "19" to 5,
        "20" to 3,
        "21" to 2,
        "22" to 3,
        "23" to 2
    )
) {
    val buckets: List<Pair<String, Int>> = (0 until 24 step 2).map { startHour ->
        val endHour = startHour + 1
        val label = String.format("%02d~%02d시", startHour, endHour)

        // Map의 키는 "10", "22" 처럼 문자열이므로 toString() 사용
        val sum = (startHour..endHour).sumOf { hour ->
            hourlyStats[hour.toString()] ?: 0
        }

        label to sum
    }

    val maxCount = (buckets.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "주요 소음 발생 시간대",
                color = Gray300,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(180.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                buckets.forEach { (label, count) ->
                    val barHeightRatio = count.toFloat() / maxCount.toFloat()
                    val barHeight = (barHeightRatio * 160).dp

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(barHeight)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(Blue500),
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = label,
                            color = Color.LightGray,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}