package com.kau.ttokttok.ui.compose.noisevote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.card.NoiseVoteBoardCard
import com.kau.ttokttok.ui.component.common.header.NoiseVoteBoardHeader
import com.kau.ttokttok.ui.theme.*

@Preview
@Composable
fun NoiseVoteScreen(
    modifier: Modifier = Modifier,
    uiState: NoiseVoteUiState = NoiseVoteUiState(),
    onClickPost: (Long) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
            .statusBarsPadding()
            .padding(bottom = 64.dp)
    ) {
        // 헤더
        NoiseVoteBoardHeader(
            title = "소음 현황판"
        )

        Spacer(Modifier.height(32.dp))

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = uiState.posts,
                key = { it.id }
            ) { report ->
                NoiseVoteBoardCard(
                    authorLocation = report.authorLocation,
                    title = report.title,
                    onClick = { onClickPost(report.id) }
                )
            }
        }
    }
}