package com.kau.ttokttok.ui.compose.monthreport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

private val Gray100 = Color(0xFFF3F4F6)
private val Gray400 = Color(0xFF9CA3AF)
private val Red400 = Color(0xFFF87171)
private val Green400 = Color(0xFF34D399)

private val Slate900 = Color(0xFF0F172A)


private val Slate800 = Color(0xFF1E293B)
private val Slate700 = Color(0xFF334155)
private val Gray300 = Color(0xFFD1D5DB)
private val Blue500 = Color(0xFF3B82F6)
private val Blue600 = Color(0xFF2563EB)  // text/btn blue-600
private val Blue400 = Color(0xFF60A5FA)  // icon blue-400

private val Purple900 = Color(0xFF4C1D95)
private val Blue900 = Color(0xFF1E3A8A)
private val Purple700 = Color(0xFF6D28D9)
private val Purple300 = Color(0xFFD8B4FE)
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

/** 편의용 오버로드: 색 지정 안 하면 기본 팔레트 순환 */
fun noiseTypeSlicesOf(pairs: List<Pair<String, Int>>): List<NoiseTypeSlice> =
    pairs.mapIndexed { idx, (t, c) ->
        NoiseTypeSlice(type = t, count = c, color = DefaultColors[idx % DefaultColors.size])
    }

@Preview(heightDp = 1300)
@Composable
fun MonthReportScreen(
    modifier: Modifier = Modifier,
    onClickMoreInformation: () -> Unit = {}
) {
    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Slate900)
        ) {
            ReportHeader()

            Spacer(modifier.height(32.dp))

            NoiseSummaryCard()

            Spacer(modifier.height(32.dp))

            AiSummaryCard()

            Spacer(modifier.height(32.dp))

            NoisePeakHoursCard()

            Spacer(modifier.height(32.dp))

            NoiseTypesCard()

            Spacer(modifier.height(32.dp))

            NationalDataPreviewCard(
                onViewNationalData = onClickMoreInformation
            )
        }
    }
}

@Composable
fun ReportHeader(
    month: String = "2025년 11월",
    modifier: Modifier = Modifier
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
                    text = month,
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

@Composable
fun NoiseSummaryCard(
    totalReports: Int = 47,
    comparedToPrevious: Int = -15, // +면 증가, -면 감소
    modifier: Modifier = Modifier
) {
    val isIncrease = comparedToPrevious > 0
    val trendColor = if (isIncrease) Red400 else Green400
    val trendIcon = if (isIncrease) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown
    val changeLabel = if (isIncrease) "증가" else "감소"

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "이달의 소음 리포트",
                        color = Gray400,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${totalReports}건",
                        color = Gray100,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = trendIcon,
                        contentDescription = if (isIncrease) "증가" else "감소",
                        tint = trendColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "${kotlin.math.abs(comparedToPrevious)}%",
                        color = trendColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                text = "전월 대비 $changeLabel",
                color = Gray400,
                fontSize = 13.sp
            )
        }
    }
}

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

data class PeakHourData(
    val hour: String,  // "09시", "10시", ...
    val count: Int     // 발생 횟수
)

@Composable
fun NoisePeakHoursCard(
    modifier: Modifier = Modifier,
    data: List<PeakHourData> = listOf(
        PeakHourData("06시", 3),
        PeakHourData("09시", 8),
        PeakHourData("12시", 5),
        PeakHourData("15시", 10),
        PeakHourData("18시", 7),
        PeakHourData("21시", 2)
    )
) {
    val maxCount = (data.maxOfOrNull { it.count } ?: 1).coerceAtLeast(1)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "주요 소음 발생 시간대",
                color = Gray300,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 간단한 바 차트 구현
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                data.forEach { item ->
                    val barHeightRatio = item.count.toFloat() / maxCount.toFloat()
                    val barHeight = (barHeightRatio * 160).dp

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // 바 (그래프 막대)
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(barHeight)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(Blue500),
                        )

                        Spacer(Modifier.height(4.dp))

                        // X축 라벨 (시간대)
                        Text(
                            text = item.hour,
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

@Composable
fun NoiseTypesCard(
    modifier: Modifier = Modifier,
    slices: List<NoiseTypeSlice> = noiseTypeSlicesOf(
        listOf(
            "아이들 뛰는 소리" to 12,
            "가구 끄는 소리" to 8,
            "악기/노래" to 5,
            "반려동물" to 3,
            "가전소음" to 2
        )
    )
) {
    val total = max(1, slices.sumOf { it.count }) // 0 나눗셈 방지

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

            // 차트 (가운데 정렬, 200dp 박스 안에 파이)
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

                    var startAngle = -90f // 위쪽(12시)부터 그리기
                    slices.forEach { slice ->
                        val sweep = (slice.count.toFloat() / total.toFloat()) * 360f
                        // 조각
                        drawArc(
                            color = slice.color,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = true,   // 파이(원형) 형태
                            size = pieSize
                        )
                        startAngle += sweep
                    }
                }
            }

            // 범례 (grid-cols-2 gap-2 mt-4) — 최대 4개 표시
            Spacer(Modifier.height(12.dp))
            val legendItems = slices.take(4)
            legendItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { s ->
                        Row(
                            modifier = Modifier
                                .weight(1f),
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
                    // 홀수 개일 때 오른쪽 빈칸 채워 그리드 정렬 유지
                    if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ---- 카드 컴포넌트 ---- */
@Composable
fun NationalDataPreviewCard(
    onViewNationalData: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Place, // MapPin 대체
                        contentDescription = null,
                        tint = Blue400,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "전국 아파트 소음 데이터",
                        color = Gray300,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onViewNationalData,
                    border = BorderStroke(1.dp, Blue600),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Blue600
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("더보기", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}