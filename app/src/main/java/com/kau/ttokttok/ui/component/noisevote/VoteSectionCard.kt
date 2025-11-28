package com.kau.ttokttok.ui.component.noisevote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.ui.theme.*

@Composable
fun VoteSectionCard(
    modifier: Modifier = Modifier,
    userVote: NoiseVoteType?,
    votes: Map<NoiseVoteType, Int>,
    totalVotes: Int,
    onVote: (NoiseVoteType) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {},
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
                    .fillMaxHeight()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoteButton(
                    modifier = Modifier
                        .weight(1f),
                    label = "들려요",
                    count = votes[NoiseVoteType.HEARD] ?: 0,
                    selected = userVote == NoiseVoteType.HEARD,
                    selectedBg = Blue50,
                    selectedBorder = Blue600,
                    selectedText = Blue600,
                    icon = Icons.Default.ThumbUp,
                    onButtonClick = {
                        onVote(NoiseVoteType.HEARD)
                    }
                )

                VoteButton(
                    modifier = Modifier
                        .weight(1f),
                    label = "안 들려요",
                    count = votes[NoiseVoteType.NOT_HEARD] ?: 0,
                    selected = userVote == NoiseVoteType.NOT_HEARD,
                    selectedBg = Gray100,
                    selectedBorder = Gray600,
                    selectedText = Gray600,
                    icon = Icons.Default.ThumbDown,
                    onButtonClick = { onVote(NoiseVoteType.NOT_HEARD) }
                )

                VoteButton(
                    modifier = Modifier
                        .weight(1f),
                    label = "주의할게요",
                    count = votes[NoiseVoteType.BE_CAREFUL] ?: 0,
                    selected = userVote == NoiseVoteType.BE_CAREFUL,
                    selectedBg = Green50,
                    selectedBorder = Green600,
                    selectedText = Green600,
                    icon = Icons.Default.Warning,
                    onButtonClick = {
                        onVote(NoiseVoteType.BE_CAREFUL)
                    }
                )
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