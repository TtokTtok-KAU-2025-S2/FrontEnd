package com.kau.ttokttok.ui.compose.preconsideration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.header.BoardHeader

private val Slate900 = Color(0xFF0F172A) // bg-slate-900


@Preview
@Composable
fun PreConsiderationScreen(
    modifier: Modifier = Modifier,
    onClickCreatePost: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Slate900)
            .padding(bottom = 64.dp)
    ) {
        // 헤더
        BoardHeader(
            onClickCreatePost = onClickCreatePost,
            title = "사전 양해 게시판",
            titleDescription = "이웃들에게 미리 알려주세요"
        )

        Spacer(Modifier.height(32.dp))
    }
}