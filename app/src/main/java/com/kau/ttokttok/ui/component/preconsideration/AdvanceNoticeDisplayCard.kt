package com.kau.ttokttok.ui.component.preconsideration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.theme.Gray700
import com.kau.ttokttok.ui.theme.Gray900
import com.kau.ttokttok.ui.theme.Orange200
import com.kau.ttokttok.ui.theme.Orange50
import com.kau.ttokttok.ui.theme.Orange900

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
            text = if (value.isBlank()) "~" else value,
            color = Gray900,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}