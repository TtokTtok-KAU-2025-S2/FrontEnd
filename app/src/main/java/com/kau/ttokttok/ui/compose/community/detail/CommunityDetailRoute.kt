package com.kau.ttokttok.ui.compose.community.detail

import androidx.compose.runtime.Composable

@Composable
fun CommunityDetailRoute(
    onClickBack: () -> Unit
) {

    CommunityDetailScreen(
        onClickBack = onClickBack
    )
}