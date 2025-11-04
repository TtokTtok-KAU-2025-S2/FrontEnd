package com.kau.ttokttok.ui.compose.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.header.BoardHeader

private val Slate900 = Color(0xFF0F172A) // bg-slate-900
private val Blue700  = Color(0xFF1D4ED8) // hover:bg-blue-700

@Preview
@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    onClickCreatePost: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)          // flex-col h-full bg-slate-900
            .padding(bottom = 64.dp)       // pb-16
    ) {
        // Header
        BoardHeader(
            onClickCreatePost = onClickCreatePost,
            title = "공지사항",
            titleDescription = "이웃과 소통하고 정보를 공유해보세요"
        )

        Spacer(Modifier.height(32.dp))
    }
}