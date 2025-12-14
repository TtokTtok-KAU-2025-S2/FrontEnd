package com.kau.ttokttok.ui.compose.noisevote.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.noisevote.*
import com.kau.ttokttok.ui.theme.*
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NoiseVoteDetailScreen(
    modifier: Modifier = Modifier,
    uiState: NoiseVoteDetailUiState = NoiseVoteDetailUiState(),
    onRefresh: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onVoteClick: (NoiseVoteType) -> Unit = {},
    onAddComment: (String) -> Unit = {},
    onEditComment: (Comment) -> Unit = {},
    onDeleteComment: (Comment) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val pullToRefreshState = rememberPullToRefreshState()

        // 상단 헤더
        WhiteHeader(
            title = "리포트 상세",
            onBack = onClickBack
        )

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray50Bg),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh
        ) {
            Column(
                modifier = Modifier
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
                    category = uiState.noiseVoteBoardDetail?.category?.label ?: "ERROR"
                )

                // 2) 투표 카드
                VoteSectionCard(
                    userVote = uiState.noiseVoteBoardDetail?.myVoteType,
                    votes = uiState.noiseVoteBoardDetail?.voteCount ?: emptyMap(),
                    totalVotes = uiState.noiseVoteBoardDetail?.voteCount?.values?.sum() ?: 0,
                    onVote = { voteType ->
                        onVoteClick(voteType)
                    }
                )

                // 3) 댓글 리스트
                CommentListCard(
                    comments = uiState.noiseVoteBoardDetail?.comments ?: emptyList(),
                    onEditComment = { comment ->
                        onEditComment(comment)
                    },
                    onDeleteComment = { comment ->
                        onDeleteComment(comment)
                    }
                )
            }

            // 하단 댓글 입력 바
            CommentInputBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .imePadding(),
                onSend = { content ->
                    onAddComment(content)
                }
            )
        }
    }
}