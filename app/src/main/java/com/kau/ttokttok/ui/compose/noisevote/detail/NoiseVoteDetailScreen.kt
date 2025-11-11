package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.data.remote.dto.noiseboard.res.Comment
import com.kau.ttokttok.data.remote.dto.noiseboard.res.VoteCount
import com.kau.ttokttok.ui.component.common.header.WhiteHeader

// ---------- 팔레트 ----------
private val Gray50Bg = Color(0xFFF9FAFB)
private val White    = Color.White
private val Gray900  = Color(0xFF111827)
private val Gray700  = Color(0xFF374151)
private val Gray600  = Color(0xFF4B5563)
private val Gray500  = Color(0xFF6B7280)
private val Gray400  = Color(0xFF9CA3AF)
private val Gray200  = Color(0xFFE5E7EB)
private val Gray100  = Color(0xFFF3F4F6)
private val Blue50   = Color(0xFFEFF6FF)
private val Blue600  = Color(0xFF2563EB)
private val Blue700  = Color(0xFF1D4ED8)
private val Green50  = Color(0xFFECFDF5)
private val Green600 = Color(0xFF16A34A)

enum class VoteType { HEARD, NOT_HEARD, WILL_BE_CAREFUL }

@Composable
fun NoiseVoteDetailScreen(
    modifier: Modifier = Modifier,
    uiState: NoiseVoteDetailUiState,
    onClickBack: () -> Unit = {},
    onVote: (VoteType) -> Unit = {},
    onAddComment: (String) -> Unit = {}
) {
    // 사용자가 누른 투표 상태(로컬 UI 전용). 서버 연동 시 viewModel에 올려도 OK.
    var selectedVote by remember { mutableStateOf<VoteType?>(null) }

    // 투표 카운트 null-safe 처리
    val votes: VoteCount = uiState.voteCount ?: VoteCount(HEARD = 0, NOT_HEARD = 0, BE_CAREFUL = 0)
    val totalVotes = votes.HEARD + votes.NOT_HEARD + votes.BE_CAREFUL

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 상단 헤더
        WhiteHeader(
            title = "리포트 상세",
            onBack = onClickBack
        )

        // 스크롤 영역(상단: 정보/투표, 하단: 댓글)
        Column(
            modifier = Modifier
                .weight(1f)
                .background(Gray50Bg)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NoiseVoteContent(
                state = uiState,
                modifier = modifier
            )

            // 2) 투표 카드
            VoteSectionCard(
                userVote = selectedVote,
                votes = votes,
                totalVotes = totalVotes,
                onVote = { type ->
                    selectedVote = type            // UI 반영
                    onVote(type)                   // 상위로 이벤트 전달(서버 반영은 Route/VM에서)
                }
            )

            // 3) 댓글 리스트
            CommentListCard(
                comments = uiState.comments
            )
        }

        // 하단 댓글 입력 바
        CommentInputBar(
            onSend = { content ->
                if (content.isNotBlank()) onAddComment(content)
            }
        )
    }
}

@Composable
private fun VoteSectionCard(
    userVote: VoteType?,
    votes: VoteCount,
    totalVotes: Int,
    onVote: (VoteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    VoteButton(
                        label = "들려요",
                        count = votes.HEARD,
                        selected = userVote == VoteType.HEARD,
                        selectedBg = Blue50,
                        selectedBorder = Blue600,
                        selectedText = Blue600,
                        icon = Icons.Default.ThumbUp,
                        onClick = { onVote(VoteType.HEARD) }
                    )
                }

                Box(
                    modifier = modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    VoteButton(
                        label = "안 들려요",
                        count = votes.NOT_HEARD,
                        selected = userVote == VoteType.NOT_HEARD,
                        selectedBg = Gray100,
                        selectedBorder = Gray600,
                        selectedText = Gray600,
                        icon = Icons.Default.ThumbDown,
                        onClick = { onVote(VoteType.NOT_HEARD) }
                    )
                }

                Box(
                    modifier = modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    VoteButton(
                        label = "주의할게요",
                        count = votes.BE_CAREFUL,
                        selected = userVote == VoteType.WILL_BE_CAREFUL,
                        selectedBg = Green50,
                        selectedBorder = Green600,
                        selectedText = Green600,
                        icon = Icons.Default.Warning,
                        onClick = { onVote(VoteType.WILL_BE_CAREFUL) }
                    )
                }
            }

            Text(
                text = "총 ${totalVotes}명이 투표에 참여했습니다",
                color = Gray600,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/** 공통 투표 버튼 UI */
@Composable
private fun VoteButton(
    label: String,
    count: Int,
    selected: Boolean,
    selectedBg: Color,
    selectedBorder: Color,
    selectedText: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = Modifier.heightIn(min = 90.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) selectedBg else Color.Transparent,
            contentColor = if (selected) selectedText else Gray600
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) selectedBorder else Gray400.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 11.sp)
            Text("$count", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun CommentListCard(
    comments: List<Comment>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp)
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
                    Text(text = "아직 댓글이 없습니다", color = Gray400, fontSize = 13.sp)
                }
            } else {
                comments.forEachIndexed { index, c ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = if (index == comments.lastIndex) 0.dp else 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${c.authorDong} 주민", color = Gray500, fontSize = 11.sp)
                            Text(text = c.createdAt, color = Gray400, fontSize = 11.sp)
                        }
                        Text(text = c.content, color = Gray900, fontSize = 13.sp)

                        if (index != comments.lastIndex) {
                            Divider(
                                modifier = Modifier.padding(top = 8.dp),
                                color = Gray100,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentInputBar(
    onSend: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
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
            androidx.compose.material3.OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("댓글을 입력하세요", fontSize = 13.sp, color = Gray400) },
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Blue600,
                    unfocusedIndicatorColor = Gray200,
                    cursorColor = Blue600
                ),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                maxLines = 3
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSend(text)
                        text = ""
                    }
                },
                enabled = text.isNotBlank(),
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

@Composable
fun NoiseVoteContent(
    state: NoiseVoteDetailUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier,
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "${state.buildingNumber}동 주민",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }

                    Box(
                        modifier = modifier,
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = state.reportedAt,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray.copy(alpha = 0.7f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Black),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Decibel Section (기본값 0dB)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "측정 소음 정보",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("최대 소음", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text("0dB", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("평균 소음", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text("0dB", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("카테고리", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(String.format("%s", state.category), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
