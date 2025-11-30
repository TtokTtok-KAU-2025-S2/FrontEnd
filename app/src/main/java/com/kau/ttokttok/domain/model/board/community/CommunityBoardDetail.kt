package com.kau.ttokttok.domain.model.board.community

import java.time.LocalDateTime

data class CommunityBoardDetail(
    val title: String,
    val createdAt: LocalDateTime,
    val content: String
)
