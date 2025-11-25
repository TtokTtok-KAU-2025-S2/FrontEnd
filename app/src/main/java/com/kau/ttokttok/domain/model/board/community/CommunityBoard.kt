package com.kau.ttokttok.domain.model.board.community

import java.time.LocalDateTime

data class CommunityBoard(
    val id: Long,
    val createdAt: LocalDateTime,
    val title: String,
)
