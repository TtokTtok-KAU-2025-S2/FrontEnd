package com.kau.ttokttok.ui.compose.preconsideration.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onClickBack: () -> Unit = { },
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Gray50Bg)
    ) {
        WhiteHeader(
            title = "사전 양해 게시판 상세",
            onBack = onClickBack,
            onEdit = onEdit,
            onDelete = { showDeleteDialog = true}
        )

        PreConsiderationBoardDetailContent(
            uiState = uiState
        )

        AdvanceNoticeDisplayCard(
            noticeDate = uiState.preConsiderationBoardDetail?.noticeDate ?: "",
            noticeTime = uiState.preConsiderationBoardDetail?.noticeTime ?: "",
            noticeReason = uiState.preConsiderationBoardDetail?.noticeReason ?: "",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
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