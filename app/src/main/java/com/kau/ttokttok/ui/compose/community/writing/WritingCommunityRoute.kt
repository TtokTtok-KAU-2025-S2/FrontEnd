package com.kau.ttokttok.ui.compose.community.writing

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WritingCommunityRoute(
    viewModel: WritingCommunityViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect((Unit)) {
        viewModel.event.collect { event ->
            when (event) {
                is WritingCommunityEvent.Success -> {
                    onClickBack
                }

                is WritingCommunityEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    WritingCommunityScreen(
        onClickCreate = {
            title, content -> viewModel.createPost(title, content)
        },
        onClickBack = onClickBack
    )
}