package com.kau.ttokttok.ui.compose.preconsideration

import androidx.compose.runtime.Composable

@Composable
fun PreConsiderationRoute(
    onClickCreatePost: () -> Unit,
    onClickPost: (Long) -> Unit
) {
    PreConsiderationScreen(
        onClickCreatePost = onClickCreatePost,
        onClickPost = onClickPost
    )
}