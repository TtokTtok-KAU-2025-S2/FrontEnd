package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.compose.community.detail.BoardDetailContent

private val Orange50  = Color(0xFFFFF7ED) // bg-orange-50
private val Orange200 = Color(0xFFFECBA1) // border-orange-200
private val Orange900 = Color(0xFF7C2D12) // text-orange-900
private val Gray700   = Color(0xFF374151) // text-gray-700
private val Gray900   = Color(0xFF111827) // text-gray-900
private val Gray50Bg  = Color(0xFFF9FAFB)

@Preview
@Composable
fun PreConsiderationDetailScreen(
    modifier: Modifier = Modifier,
    uiState: PreConsiderationDetailUiState,
    onClickBack: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray50Bg) // 카드 대비를 위해 밝은 배경 권장
    ) {
        WhiteHeader(
            title = "사전 양해 게시판 상세",
            onBack = onClickBack
        )

        // 이 화면의 다른 상세(제목/내용 등)
        BoardDetailContent(
            title = uiState.title,
            content = uiState.content
        )

        // 🔸 읽기 전용 사전 양해 카드 (항상 표시)
        AdvanceNoticeDisplayCard(
            noticeDate = uiState.noticeDate,
            noticeTime = uiState.noticeTime,
            noticeReason = uiState.noticeReason,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

/**
 * 🧾 사전 양해 상세 정보 (전시용 / 항상 표시)
 * - 입력필드 없이 라벨 + 값 텍스트만 보여줌
 */
@Composable
fun AdvanceNoticeDisplayCard(
    noticeDate: String,
    noticeTime: String,
    noticeReason: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Orange50),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Orange200),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "사전 양해 상세 정보",
                color = Orange900,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            // 예정일
            DisplayRow(label = "예정일", value = noticeDate)

            // 시간
            DisplayRow(label = "시간", value = noticeTime)

            // 사유
            DisplayRow(label = "사유", value = noticeReason)
        }
    }
}

/** 라벨 + 값 (전시용) */
@Composable
private fun DisplayRow(
    label: String,
    value: String,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Gray700,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = if (value.isBlank()) "-" else value,
            color = Gray900,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
