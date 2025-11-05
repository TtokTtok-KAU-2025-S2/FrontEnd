package com.kau.ttokttok.ui.compose.community

import androidx.compose.runtime.Composable

@Composable
fun CommunityRoute(
    onClickCreatePost: () -> Unit
) {
    CommunityScreen(
        onClickCreatePost = onClickCreatePost
    )
}