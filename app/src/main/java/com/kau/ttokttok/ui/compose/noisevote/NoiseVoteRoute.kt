package com.kau.ttokttok.ui.compose.noisevote

import androidx.compose.runtime.Composable

@Composable
fun NoiseVoteRoute(
    onClickPost: (Long) -> Unit
) {
    NoiseVoteScreen(
        onClickPost = onClickPost
    )
}