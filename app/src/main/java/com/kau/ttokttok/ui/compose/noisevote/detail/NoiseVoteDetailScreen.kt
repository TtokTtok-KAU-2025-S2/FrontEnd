package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.noisevote.CommentInputBar
import com.kau.ttokttok.ui.component.noisevote.CommentListCard
import com.kau.ttokttok.ui.component.noisevote.NoiseVoteContent
import com.kau.ttokttok.ui.component.noisevote.VoteSectionCard
import com.kau.ttokttok.ui.theme.*
import java.time.LocalDateTime

@Preview
@Composable
fun NoiseVoteDetailScreen(
    modifier: Modifier = Modifier,
    uiState: NoiseVoteDetailUiState = NoiseVoteDetailUiState(),
    onClickBack: () -> Unit = {},
    onVoteClick: (NoiseVoteType) -> Unit,
    onAddComment: (String) -> Unit,
    onEditComment: (Comment) -> Unit,
    onDeleteComment: (Comment) -> Unit
) {
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
                modifier = modifier,
                buildingNumber = uiState.noiseVoteBoardDetail?.buildingNumber ?: 0,
                title = uiState.noiseVoteBoardDetail?.title ?: "",
                reportedAt = (uiState.noiseVoteBoardDetail?.reportedAt ?: LocalDateTime.now()) as LocalDateTime,
                maxDb = uiState.noiseVoteBoardDetail?.maxDb ?: 0,
                avgDb = uiState.noiseVoteBoardDetail?.avgDb ?: 0,
                category = uiState.noiseVoteBoardDetail?.category ?: ""
            )

            // 2) 투표 카드
            VoteSectionCard(
                userVote = uiState.selectedVote,
                votes = uiState.noiseVoteBoardDetail?.voteCount ?: emptyMap(),
                totalVotes = uiState.noiseVoteBoardDetail?.voteCount?.values?.sum() ?: 0,
                onVote = { type ->
                    onVoteClick(type)
                }
            )

            // 3) 댓글 리스트
            CommentListCard(
                comments = uiState.noiseVoteBoardDetail?.comments ?: emptyList(),
                onEditComment = onEditComment,
                onDeleteComment = onDeleteComment
            )
        }

        // 하단 댓글 입력 바
        CommentInputBar(
            onSend = { content ->
                if (content.isNotBlank())
                    onAddComment(content)
            }
        )
    }
}