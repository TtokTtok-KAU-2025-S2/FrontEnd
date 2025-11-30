package com.kau.ttokttok.data.remote.dto.noiseboard.res

data class ModifyCommentRes(
    val commentId: Long,
    val authorDong: String,
    val content: String,
    val createdAt: String,
    val isMyComment: Boolean
)
