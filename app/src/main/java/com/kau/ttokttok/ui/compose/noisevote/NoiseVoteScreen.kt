package com.kau.ttokttok.ui.compose.noisevote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.card.NoiseVoteBoardCard
import com.kau.ttokttok.ui.component.common.header.NoiseVoteBoardHeader

private val Slate900 = Color(0xFF0F172A) // bg-slate-900

// 예시 데이터 모델
data class NoiseReport(
    val id: Long,
    val authorLocation: String,
    val summary: String
)

@Preview
@Composable
fun NoiseVoteScreen(
    modifier: Modifier = Modifier,
    onClickPost: (Long) -> Unit = { },
    posts: List<NoiseReport> = emptyList()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
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
                items = posts,
                key = { it.id }
            ) { report ->
                NoiseVoteBoardCard(
                    authorLocation = report.authorLocation,
                    title = report.summary,
                    onClick = { onClickPost(report.id) }
                )
            }
        }
    }
}