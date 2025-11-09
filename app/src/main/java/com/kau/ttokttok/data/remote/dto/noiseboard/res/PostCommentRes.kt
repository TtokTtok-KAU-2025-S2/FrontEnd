package com.kau.ttokttok.data.remote.dto.noiseboard.res

import java.time.LocalDateTime

data class PostCommentRes(
    val commentId: Long,
    val authorDong: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val isMyComment: Boolean
)
