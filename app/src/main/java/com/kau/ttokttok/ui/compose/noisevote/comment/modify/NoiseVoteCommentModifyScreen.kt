package com.kau.ttokttok.ui.compose.noisevote.comment.modify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.button.PostBottomActions
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.common.textfield.PostContentField
import com.kau.ttokttok.ui.theme.*

@Preview
@Composable
fun NoiseVoteCommentModifyScreen(
    modifier: Modifier = Modifier,
    uiState: NoiseVoteCommentModifyUiState = NoiseVoteCommentModifyUiState(),
    onClickModify: (String) -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    var content by remember { mutableStateOf("")}

    LaunchedEffect(uiState.commentContent) {
        val contentDetail = uiState.commentContent ?: return@LaunchedEffect

        content = contentDetail
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        WhiteHeader(
            title = "댓글 수정",
            onBack = onClickBack
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(White)
        ) {
            Spacer(modifier.height(32.dp))

            PostContentField(
                content = content,
                onValueChange = { content = it }
            )

            Spacer(modifier.height(32.dp))

            PostBottomActions(
                enabled = content.isNotBlank(),
                onSubmit = {
                    onClickModify(content)
                }
            )
        }
    }
}