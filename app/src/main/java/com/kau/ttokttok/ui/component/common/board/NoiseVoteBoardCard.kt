package com.kau.ttokttok.ui.component.common.board

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok._core.util.DateUtils.formatDateTime
import com.kau.ttokttok.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun NoiseVoteBoardCard(
    modifier: Modifier = Modifier,
    authorLocation: String = "",
    reportedAt: LocalDateTime = LocalDateTime.now(),
    title: String = "123456",
    comment: Int = 0,
    current: Int = 6,
    total: Int = 12,
    onClick: () -> Unit = {}
) {
    val progress = remember(current, total) {
        if (total <= 0) 0f else (current.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 상단 메타
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${authorLocation} 주민",
                    color = Gray400,
                    fontSize = 12.sp
                )

                Spacer(modifier.weight(1f))

                Text(
                    text = reportedAt.formatDateTime(),
                    color = Gray400,
                    fontSize = 12.sp
                )
            }

            // 요약
            Text(
                text = title,
                color = Gray100,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(32.dp))

            // 투표
            Column {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Blue600,
                    trackColor = Gray400
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChatBubbleOutline,   // 💬 댓글 아이콘
                        contentDescription = null,
                        tint = Gray400,                                 // 회색 톤 아이콘
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "댓글 ${comment}개",
                        color = Gray400,
                        fontSize = 11.sp
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "$current / $total   ${(progress * 100).toInt()}%",
                        color = Blue400,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
