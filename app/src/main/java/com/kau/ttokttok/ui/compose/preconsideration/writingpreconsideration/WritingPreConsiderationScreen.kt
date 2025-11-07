package com.kau.ttokttok.ui.compose.preconsideration.writingpreconsideration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.compose.community.writingcommunity.PostBottomActions
import com.kau.ttokttok.ui.compose.community.writingcommunity.PostContentField
import com.kau.ttokttok.ui.compose.community.writingcommunity.PostTitleField
import java.time.LocalDate
import java.time.LocalDateTime

private val Gray700 = Color(0xFF374151)
private val Gray200 = Color(0xFFE5E7EB)
private val White = Color.White
private val Gray400 = Color(0xFF9CA3AF)
private val Blue600 = Color(0xFF2563EB)
private val Orange50  = Color(0xFFFFF7ED) // bg-orange-50
private val Orange200 = Color(0xFFFECBA1) // border-orange-200
private val Orange900 = Color(0xFF7C2D12) // text-orange-900
@Preview
@Composable
fun WritingPreConsiderationScreen(
    modifier: Modifier = Modifier,
    onClickCreate: (String, String, LocalDate, LocalDateTime, LocalDateTime, String) -> Unit = {_, _, _, _, _, _ -> },
    onClickBack: () -> Unit = { }
) {
    var title by remember { mutableStateOf("")}
    var content by remember { mutableStateOf("")}
    var noticeDate by remember { mutableStateOf("연도-월-일")}
    var noticeTime by remember { mutableStateOf("00:00 - 00:00")}
    var noticeReason by remember { mutableStateOf("")}

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 헤더
        WhiteHeader(
            onBack = onClickBack
        )

        Spacer(Modifier.height(32.dp))

        // 제목 입력
        PostTitleField(
            title = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(32.dp))

        PostContentField(
            content = content,
            onValueChange = { content = it }
        )

        Spacer(Modifier.height(32.dp))

        AdvanceNoticeSection(
            noticeDate = noticeDate,
            onNoticeDateChange = { noticeDate = it},
            noticeTime = noticeTime,
            onNoticeTimeChange =  { noticeTime = it},
            noticeReason = noticeReason,
            onNoticeReasonChange = { noticeReason = it }
        )

        Spacer(Modifier.height(32.dp))

        PostBottomActions(
            enabled = title.isNotBlank() && content.isNotBlank(),
            onSubmit = { }
        )
    }
}

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
                placeholder = "예: 2025-11-06",
                // ⬇️ DatePicker를 붙이고 싶다면 여기서 readOnly + trailingIcon으로 Dialog 열기 처리
                readOnly = false
            )

            // 시간
            LabeledField(
                label = "시간",
                value = noticeTime,
                onValueChange = onNoticeTimeChange,
                placeholder = "예: 10:00 - 17:00"
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

/** 공통 라벨 + OutlinedTextField */
@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Gray700,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(8.dp)),
            readOnly = readOnly,
            singleLine = true,
            placeholder = { Text(placeholder, color = Gray700.copy(alpha = 0.5f)) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Gray700,
                unfocusedIndicatorColor = Gray200
            )
        )
    }
}
