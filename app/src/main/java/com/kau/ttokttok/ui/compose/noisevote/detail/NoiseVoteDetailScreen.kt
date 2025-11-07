package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private val Gray50  = Color(0xFFFAFAFA) // bg-gray-50
private val White   = Color.White       // bg-white
private val Gray900 = Color(0xFF111827) // text-gray-900
private val Gray700 = Color(0xFF374151) // text-gray-700
private val Gray500 = Color(0xFF6B7280) // text-gray-500
private val Gray400 = Color(0xFF9CA3AF) // text-gray-400
private val Gray200 = Color(0xFFE5E7EB) // border-gray-200
private val Gray50Bg = Color(0xFFF9FAFB)
private val Gray600     = Color(0xFF4B5563)
private val Blue50      = Color(0xFFEFF6FF)
private val Blue600     = Color(0xFF2563EB)
private val Gray100     = Color(0xFFF3F4F6)
private val Gray600Border = Color(0xFF4B5563)
private val Green50     = Color(0xFFECFDF5)
private val Green600    = Color(0xFF16A34A)
private val Blue700 = Color(0xFF1D4ED8)


@Preview
@Composable
fun NoiseVoteDetailScreen(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {},
    noiseReportDetail: NoiseReportDetail = NoiseReportDetail(
        authorLocation = "302동 1203호",
        createdAtMillis = System.currentTimeMillis(),
        summary = "어젯밤 10시경 가구 끄는 소리와 물건 떨어지는 소리가 반복적으로 발생했습니다.",
        logs = listOf(
            NoiseLog(id = "2", maxDecibel = 84, avgDecibel = 68, noiseType = "물체 낙하", durationSeconds = 95)
        )
    )
) {
    var selectedVote by remember { mutableStateOf<VoteType?>(null)}
    var votes = VoteInfo(heard = 23, notHeard = 12, willBeCareful = 8)
    val comments = remember {
        mutableStateListOf(
            Comment("1", "302동", System.currentTimeMillis() - 3600000, "어제 밤에 저도 들었어요."),
            Comment("2", "303동", System.currentTimeMillis() - 1800000, "이웃 간 예의 지켜주세요.")
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        WhiteHeader(
            title = "리포트 상세",
            onBack = onClickBack
        )

        ReportDetailContent(
            report = noiseReportDetail,
            formatDate = "2025.11.07 19:20"
        )

        VoteSectionCard(
            userVote = selectedVote,
            votes = votes,
            totalVotes = votes.heard + votes.notHeard + votes.willBeCareful,
            onVote = { voteType ->
                selectedVote = voteType // UI 상태 변경
            }
        )

        CommentSection(
            comments = comments,
            formatDate = "",
            onAddComment = {_ -> }
        )
    }


}

/* 데이터 모델 */
data class NoiseLog(
    val id: String,
    val maxDecibel: Int,
    val avgDecibel: Int,
    val noiseType: String,
    val durationSeconds: Int,
)

data class NoiseReportDetail(
    val authorLocation: String, // "302동 1203호" 등
    val createdAtMillis: Long,
    val summary: String,
    val logs: List<NoiseLog>
)

/** 외곽 스크롤 컨테이너(overflow-auto p-4 space-y-4 pb-24) */
@Composable
fun ReportDetailContent(
    report: NoiseReportDetail,
    formatDate: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray50Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp) // p-4
            .padding(bottom = 96.dp) // pb-24
    ) {
        ReportInfoCard(
            report = report,
            formatDate = formatDate
        )
    }
}

/** 화이트 카드 내부 구성 */
@Composable
private fun ReportInfoCard(
    report: NoiseReportDetail,
    formatDate: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 상단 메타: 위치 / 날짜
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${report.authorLocation} 주민",
                    color = Gray500,
                    fontSize = 12.sp
                )
                Text(
                    text = formatDate,
                    color = Gray400,
                    fontSize = 11.sp
                )
            }

            // 요약
            Text(
                text = report.summary,
                color = Gray900,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 로그들
            report.logs.forEachIndexed { index, log ->
                NoiseLogBlock(log = log)

                if (index != report.logs.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

/** 각 로그 블록 (bg-gray-50 p-3 rounded-lg space-y-2) */
@Composable
private fun NoiseLogBlock(log: NoiseLog) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray50, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        // 2열 그리드 (grid-cols-2 gap-3 text-sm)
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoKV(label = "최대 소음", value = "${log.maxDecibel}dB", modifier = Modifier.weight(1f))
                InfoKV(label = "평균 소음", value = "${log.avgDecibel}dB", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoKV(label = "소음 유형", value = log.noiseType, modifier = Modifier.weight(1f))
            }
        }
    }
}

/** 라벨 + 값 한 쌍 (text-xs / text-gray-500, text-gray-900) */
@Composable
private fun InfoKV(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = label, color = Gray500, fontSize = 11.sp)
        Text(text = value, color = Gray900, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

/* 데이터 모델 */
enum class VoteType { HEARD, NOT_HEARD, WILL_BE_CAREFUL }

data class VoteInfo(
    val heard: Int,
    val notHeard: Int,
    val willBeCareful: Int
)

/** 🗳️ 주민 투표 카드 */
@Composable
fun VoteSectionCard(
    userVote: VoteType?,
    votes: VoteInfo,
    totalVotes: Int,
    onVote: (VoteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)), // border-gray-200
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "주민 투표",
                color = Gray700,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 투표 버튼 3개 (grid-cols-3 gap-2)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoteButton(
                    label = "들려요",
                    count = votes.heard,
                    selected = userVote == VoteType.HEARD,
                    selectedBg = Blue50,
                    selectedBorder = Blue600,
                    selectedText = Blue600,
                    icon = Icons.Default.ThumbUp,
                    onClick = { onVote(VoteType.HEARD) }
                )

                VoteButton(
                    label = "안 들려요",
                    count = votes.notHeard,
                    selected = userVote == VoteType.NOT_HEARD,
                    selectedBg = Gray100,
                    selectedBorder = Gray600Border,
                    selectedText = Gray600,
                    icon = Icons.Default.ThumbDown,
                    onClick = { onVote(VoteType.NOT_HEARD) }
                )

                VoteButton(
                    label = "주의할게요",
                    count = votes.willBeCareful,
                    selected = userVote == VoteType.WILL_BE_CAREFUL,
                    selectedBg = Green50,
                    selectedBorder = Green600,
                    selectedText = Green600,
                    icon = Icons.Default.Warning,
                    onClick = { onVote(VoteType.WILL_BE_CAREFUL) }
                )
            }

            // 총 투표수
            Text(
                text = "총 ${totalVotes}명이 투표에 참여했습니다",
                color = Gray600,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/** 공통 투표 버튼 */
@Composable
private fun VoteButton(
    modifier: Modifier = Modifier,
    label: String,
    count: Int,
    selected: Boolean,
    selectedBg: Color,
    selectedBorder: Color,
    selectedText: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = 90.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) selectedBg else Color.Transparent,
            contentColor = if (selected) selectedText else Gray600
        ),
        border = BorderStroke(
            1.dp,
            if (selected) selectedBorder else Gray400.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
            Text(label, fontSize = 11.sp)
            Text("$count", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

data class Comment(
    val id: String,
    val authorLocation: String,
    val createdAt: Long,
    val content: String
)

/** 🗨️ 댓글 목록 + 입력창 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSection(
    comments: List<Comment>,
    onAddComment: (String) -> Unit,
    modifier: Modifier = Modifier,
    formatDate: String
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // 댓글 목록 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(0.5.dp, Gray200)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "댓글 ${comments.size}",
                    color = Gray700,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (comments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "아직 댓글이 없습니다",
                            color = Gray400,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    comments.forEachIndexed { index, comment ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (index == comments.lastIndex) 0.dp else 12.dp)
                        ) {
                            // 작성자 / 날짜
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${comment.authorLocation} 주민",
                                    color = Gray500,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "NULL",
                                    color = Gray400,
                                    fontSize = 11.sp
                                )
                            }

                            // 내용
                            Text(
                                text = comment.content,
                                color = Gray900,
                                fontSize = 13.sp
                            )

                            if (index != comments.lastIndex) {
                                Divider(
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    color = Gray100,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 댓글 입력 영역
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(0.5.dp, Gray200),
            color = White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("댓글을 입력하세요", fontSize = 13.sp, color = Gray400) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedIndicatorColor = Blue600,
                        unfocusedIndicatorColor = Gray200,
                        cursorColor = Blue600
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (commentText.isNotBlank()) {
                                onAddComment(commentText)
                                commentText = ""
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            }
                        }
                    ),
                    maxLines = 3
                )

                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onAddComment(commentText)
                            commentText = ""
                            coroutineScope.launch {
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }
                    },
                    enabled = commentText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue600,
                        disabledContainerColor = Blue700.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "댓글 전송",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}