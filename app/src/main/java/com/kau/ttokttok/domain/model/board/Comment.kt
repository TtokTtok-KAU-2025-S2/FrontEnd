package com.kau.ttokttok.domain.model.board

import java.time.LocalDateTime

data class Comment(
    val buildingNumber: String,
    val localDateTime: LocalDateTime,
    val content: String
)
