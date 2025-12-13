package com.kau.ttokttok.ui.compose.preconsideration.writing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.button.PostBottomActions
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.common.textfield.*
import com.kau.ttokttok.ui.component.preconsideration.AdvanceNoticeSection
import com.kau.ttokttok.ui.theme.*

@Preview
@Composable
fun WritingPreConsiderationScreen(
    modifier: Modifier = Modifier,
    uiState: WritingPreConsiderationUiState = WritingPreConsiderationUiState(),
    isEdit: Boolean = false,
    onClickCreate: (String, String, String, String, String) -> Unit = {_, _, _, _, _ -> },
    onClickModify: (String, String, String, String, String) -> Unit = {_, _, _, _, _ -> },
    onClickBack: () -> Unit = { }
) {
    var title by remember { mutableStateOf("")}
    var content by remember { mutableStateOf("")}
    var noticeDate by remember { mutableStateOf("연도.월.일")}
    var noticeTime by remember { mutableStateOf("00:00 ~ 00:00")}
    var noticeReason by remember { mutableStateOf("")}

    LaunchedEffect(uiState.preConsiderationBoardDetail) {
        val detail = uiState.preConsiderationBoardDetail ?: return@LaunchedEffect

        title = detail.title
        content = detail.content
        noticeDate = detail.noticeDate
        noticeTime = detail.noticeTime
        noticeReason = detail.noticeReason
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(White)
    ) {
        // 헤더
        WhiteHeader(
            title = if (isEdit) "게시글 수정" else "게시글 작성",
            onBack = onClickBack
        )

        Spacer(Modifier.height(32.dp))

        // 제목 입력
        PostTitleField(
            title = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(32.dp))

        PostContentField(
            content = content,
            onValueChange = { content = it }
        )

        Spacer(Modifier.height(32.dp))

        AdvanceNoticeSection(
            noticeDate = noticeDate,
            onNoticeDateChange = { noticeDate = it},
            noticeTime = noticeTime,
            onNoticeTimeChange =  { noticeTime = it},
            noticeReason = noticeReason,
            onNoticeReasonChange = { noticeReason = it }
        )

        Spacer(Modifier.height(32.dp))

        PostBottomActions(
            enabled = title.isNotBlank() && content.isNotBlank() && noticeDate.isNotBlank() && noticeTime.isNotBlank() && noticeReason.isNotBlank(),
            onSubmit = {
                if (isEdit) {
                    onClickModify(title, content, noticeDate, noticeTime, noticeReason)
                }

                else {
                    onClickCreate(title, content, noticeDate, noticeTime, noticeReason)
                }

            }
        )
    }
}