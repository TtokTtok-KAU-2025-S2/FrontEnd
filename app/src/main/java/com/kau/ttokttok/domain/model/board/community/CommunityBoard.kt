package com.kau.ttokttok.domain.model.board.community

import java.time.LocalDateTime

data class CommunityBoard(
    val id: Long,
    val buildingNumber: String,
    val createdAt: LocalDateTime,
    val title: String,
    val content: String
)
