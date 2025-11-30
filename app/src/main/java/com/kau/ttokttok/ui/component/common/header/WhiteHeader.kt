package com.kau.ttokttok.ui.component.common.header

import androidx.compose.runtime.Composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Gray200 = Color(0xFFE5E7EB)
private val Gray900 = Color(0xFF111827)
private val White = Color.White

@Preview
@Composable
fun WhiteHeader(
    modifier: Modifier = Modifier,
    title: String = "게시글 작성",
    onBack: () -> Unit = {},
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .border(BorderStroke(0.5.dp, Gray200))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(36.dp)
                .padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Gray900,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            color = Gray900,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = modifier.weight(1f))

        if (onEdit != null || onDelete != null) {
            Box {
                IconButton(
                    onClick = { menuExpanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "메뉴",
                        tint = Gray900
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    onEdit?.let {
                        DropdownMenuItem(
                            text = { Text("수정하기") },
                            onClick = {
                                menuExpanded = false
                                it()
                            }
                        )
                    }

                    onDelete?.let {
                        DropdownMenuItem(
                            text = { Text("삭제하기", color = Color.Red) },
                            onClick = {
                                menuExpanded = false
                                it()
                            }
                        )
                    }
                }
            }
        }
    }
}