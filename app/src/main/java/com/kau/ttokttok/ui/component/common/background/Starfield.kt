package com.kau.ttokttok.ui.component.common.background

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun StarField(
    modifier: Modifier = Modifier,
    starCount: Int = 200
) {
    val stars = remember {
        List(starCount) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                r = Random.nextFloat() * 2f + 0.5f,
                a = Random.nextFloat() * 0.5f + 0.5f
            )
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        stars.forEach { s ->
            drawCircle(
                color = Color.White.copy(alpha = s.a),
                radius = s.r,
                center = Offset(s.x * w, s.y * h)
            )
        }
    }
}

private data class Star(val x: Float, val y: Float, val r: Float, val a: Float)
