package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.preconsideration.AdvanceNoticeDisplayCard
import com.kau.ttokttok.ui.component.preconsideration.PreConsiderationBoardDetailContent
import com.kau.ttokttok.ui.theme.*

@Preview
@Composable
fun PreConsiderationDetailScreen(
    modifier: Modifier = Modifier,
    uiState: PreConsiderationDetailUiState = PreConsiderationDetailUiState(),
    onClickBack: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Gray50Bg) // 카드 대비를 위해 밝은 배경 권장
    ) {
        WhiteHeader(
            title = "사전 양해 게시판 상세",
            onBack = onClickBack
        )

        // 이 화면의 다른 상세(제목/내용 등)
        PreConsiderationBoardDetailContent(
            uiState = uiState
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