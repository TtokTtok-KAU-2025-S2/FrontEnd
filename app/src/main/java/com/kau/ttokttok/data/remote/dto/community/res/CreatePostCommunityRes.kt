package com.kau.ttokttok.data.remote.dto.community.res

import java.time.LocalDateTime

data class CreatePostCommunityRes(
    val noticeId: Long,
    val authorName: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val createdAt: LocalDateTime
)
