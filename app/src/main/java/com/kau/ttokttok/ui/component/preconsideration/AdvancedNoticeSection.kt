package com.kau.ttokttok.ui.component.preconsideration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.theme.*

@Composable
fun AdvanceNoticeSection(
    noticeDate: String,
    onNoticeDateChange: (String) -> Unit,
    noticeTime: String,
    onNoticeTimeChange: (String) -> Unit,
    noticeReason: String,
    onNoticeReasonChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Orange50),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Orange200),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // space-y-4
        ) {
            Text(
                text = "사전 양해 상세 정보",
                color = Orange900,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            // 예정일
            LabeledField(
                label = "예정일",
                value = noticeDate,
                onValueChange = onNoticeDateChange,
                placeholder = "예: 2025.11.06",
                // ⬇️ DatePicker를 붙이고 싶다면 여기서 readOnly + trailingIcon으로 Dialog 열기 처리
                readOnly = false
            )

            // 시간
            LabeledField(
                label = "시간",
                value = noticeTime,
                onValueChange = onNoticeTimeChange,
                placeholder = "예: 10:00 ~ 17:00"
            )

            // 사유
            LabeledField(
                label = "사유",
                value = noticeReason,
                onValueChange = onNoticeReasonChange,
                placeholder = "예: 인테리어 공사"
            )
        }
    }
}