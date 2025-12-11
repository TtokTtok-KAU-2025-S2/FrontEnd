package com.kau.ttokttok.ui.compose.noisevote.comment.modify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.compose.community.writing.PostBottomActions
import com.kau.ttokttok.ui.compose.community.writing.PostContentField
import com.kau.ttokttok.ui.theme.White

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
            .background(White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        WhiteHeader(
            title = "댓글 수정",
            onBack = onClickBack
        )

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