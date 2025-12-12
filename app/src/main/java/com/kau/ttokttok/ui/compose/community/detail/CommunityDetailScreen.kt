package com.kau.ttokttok.ui.compose.community.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.community.writing.BoardDetailContent

@Preview
@Composable
fun CommunityDetailScreen(
    modifier: Modifier = Modifier,
    uiState: CommunityDetailUiState = CommunityDetailUiState(),
    onClickBack: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        WhiteHeader(
            title = "게시판 상세",
            onBack = onClickBack
        )

        BoardDetailContent(
            communityBoardDetail = uiState.communityBoardDetail,
            isLoading = uiState.isLoading,
            onRefresh = onRefresh
        )
    }
}

