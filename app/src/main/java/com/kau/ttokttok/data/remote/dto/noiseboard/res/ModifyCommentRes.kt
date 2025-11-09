package com.kau.ttokttok.data.remote.dto.noiseboard.res

import java.time.LocalDateTime

data class ModifyCommentRes(
    val commentId: Long,
    val authorDong: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isMyComment: Boolean
)
