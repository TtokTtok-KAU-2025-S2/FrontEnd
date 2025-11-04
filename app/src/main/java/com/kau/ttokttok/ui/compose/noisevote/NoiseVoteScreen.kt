package com.kau.ttokttok.ui.compose.noisevote

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
import com.kau.ttokttok.ui.component.common.header.NoiseVoteBoardHeader

private val Slate900 = Color(0xFF0F172A) // bg-slate-900

@Preview
@Composable
fun NoiseVoteScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(bottom = 64.dp)
    ) {
        // 헤더
        NoiseVoteBoardHeader(
            title = "소음 현황판"
        )

        Spacer(Modifier.height(32.dp))
    }
}