package com.kau.ttokttok.ui.component.common.board

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Slate800 = Color(0xFF1E293B)
private val Slate700 = Color(0xFF334155)
private val Gray400 = Color(0xFF9CA3AF)
private val Gray100 = Color(0xFFF3F4F6)
private val Orange500 = Color(0xFFF97316) // border-l-orange-500

@Composable
fun PreConsiderationBoardCard(
    title: String,
    authorLocation: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Slate700),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp) // border-l-4 과 유사
                    .fillMaxHeight()
                    .background(Orange500)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // 상단 메타 정보
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${authorLocation} 주민",
                        color = Gray400,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(6.dp))

                // 제목
                Text(
                    text = title,
                    color = Gray100,
                    fontSize = 15.sp
                )
            }
        }
    }
}
