package com.kau.ttokttok.ui.component.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Slate800 = Color(0xFF1E293B) // bg-slate-800
private val Slate700 = Color(0xFF334155) // border-slate-700
private val Gray100  = Color(0xFFF3F4F6) // text-gray-100
private val Gray400  = Color(0xFF9CA3AF) // text-gray-400
private val Blue600  = Color(0xFF2563EB) // bg-blue-600

@Composable
fun NoiseVoteBoardHeader(
    title: String = ""
) {
    // Header
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate800)      // bg-slate-800
            // border-b border-slate-700
            .drawBehind {
                val stroke = 1.dp.toPx()
                drawLine(
                    color = Slate700,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = stroke
                )
            }
            .padding(16.dp)            // p-4
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = Gray100, // text-gray-100
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // TODO: 필터 추가
    }
}