package com.kau.ttokttok.ui.compose.community.writing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WritingCommunityRoute(
    viewModel: WritingCommunityViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    LaunchedEffect(Unit) {



    }

    WritingCommunityScreen(
        onClickCreate = {
            title, content -> viewModel.createPost(title, content)
        },
        onClickBack = onClickBack
    )
}