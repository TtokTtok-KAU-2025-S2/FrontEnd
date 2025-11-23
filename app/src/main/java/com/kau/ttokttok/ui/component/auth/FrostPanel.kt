package com.kau.ttokttok.ui.component.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FrostedPanel(
    modifier: Modifier = Modifier,
    corner: RoundedCornerShape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val panelColor = Color.White.copy(alpha = 0.10f)   // bg-white/10
    val borderColor = Color.White.copy(alpha = 0.20f)  // border-white/20

    // shadow-2xl 느낌 → shadowElevation 크게
    Surface(
        shape = corner,
        color = panelColor,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .clip(corner)
    ) {
        Column(Modifier.padding(24.dp)) { // p-8
            content()
        }
    }
}