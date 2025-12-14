package com.kau.ttokttok.ui.component.noisevote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.ui.theme.*
import java.time.format.DateTimeFormatter

@Composable
fun CommentListCard(
    comments: List<Comment>,
    onEditComment: (Comment) -> Unit,
    onDeleteComment: (Comment) -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteTarget by remember { mutableStateOf<Comment?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "댓글 ${comments.size}",
                color = Gray700,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "아직 댓글이 없습니다", color = Gray400, fontSize = 13.sp)
                }
            } else {
                comments.forEachIndexed { index, comment ->

                    var menuExpanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = if (index == comments.lastIndex) 0.dp else 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${comment.buildingNumber}동 주민",
                                color = Gray500,
                                fontSize = 11.sp
                            )

                            Text(
                                text = comment.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")),
                                color = Gray400,
                                fontSize = 11.sp
                            )

                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = comment.content,
                                color = Gray900,
                                fontSize = 13.sp
                            )

                            if (comment.isMyComment) {
                                Box {
                                    IconButton(
                                        onClick = { menuExpanded = true },
                                        modifier = Modifier.padding(start = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "댓글 옵션",
                                            tint = Gray400
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = menuExpanded,
                                        onDismissRequest = { menuExpanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("수정하기") },
                                            onClick = {
                                                menuExpanded = false
                                                onEditComment(comment)
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("삭제하기", color = Red400) },
                                            onClick = {
                                                menuExpanded = false
                                                deleteTarget = comment
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if (index != comments.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 8.dp),
                                thickness = 1.dp,
                                color = Gray100
                            )
                        }
                    }
                }
            }
        }
    }

    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("댓글 삭제") },
            text = {
                Text(
                    text = "댓글을 삭제하시겠습니까?\n삭제 후에는 되돌릴 수 없습니다.",
                    fontSize = 13.sp,
                    color = Gray700
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTarget?.let { onDeleteComment(it) }
                        deleteTarget = null
                    }
                ) {
                    Text("삭제", color = Red400)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("취소", color = Gray500)
                }
            }
        )
    }
}