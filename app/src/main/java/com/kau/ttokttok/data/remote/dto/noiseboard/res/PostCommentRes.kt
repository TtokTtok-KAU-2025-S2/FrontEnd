package com.kau.ttokttok.data.remote.dto.noiseboard.res

data class PostCommentRes(
    val commentId: Long,
    val authorDong: Int,
    val content: String,
    val createdAt: String,
    val isMyComment: Boolean
)
