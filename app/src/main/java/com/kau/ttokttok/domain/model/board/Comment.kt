package com.kau.ttokttok.domain.model.board

import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val buildingNumber: Int,
    val createdAt: LocalDateTime,
    val content: String,
    val isMyComment: Boolean
)
