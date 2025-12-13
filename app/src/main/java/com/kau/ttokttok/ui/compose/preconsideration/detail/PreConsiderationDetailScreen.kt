package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.preconsideration.*
import com.kau.ttokttok.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreConsiderationDetailScreen(
    modifier: Modifier = Modifier,
    uiState: PreConsiderationDetailUiState = PreConsiderationDetailUiState(),
    onRefresh: () -> Unit = {},
    onClickBack: () -> Unit = { },
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Gray50Bg)
    ) {
        WhiteHeader(
            title = "사전 양해 게시판 상세",
            onBack = onClickBack,
            onEdit = onEdit,
            onDelete = { showDeleteDialog = true}
        )

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh
        ) {
            PreConsiderationBoardDetailContent(
                uiState = uiState
            )

            AdvanceNoticeDisplayCard(
                noticeDate = uiState.preConsiderationBoardDetail?.noticeDate ?: "ERROR",
                noticeTime = uiState.preConsiderationBoardDetail?.noticeTime ?: "ERROR",
                noticeReason = uiState.preConsiderationBoardDetail?.noticeReason ?: "ERROR",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false }, // 바깥 터치 / back 버튼

            title = { Text("게시글 삭제") },
            text = { Text("정말로 이 게시글을 삭제하시겠습니까?") },

            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("삭제", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    } // 🔹 그냥 닫기 (삭제 X)
                ) {
                    Text("취소")
                }
            }
        )
    }
}