package com.kau.ttokttok.domain.model.board

import java.time.LocalDateTime

data class Board(
    override val buildingNumber: String,
    override val localDateTime: LocalDateTime,
    override val title: String,
    override val content: String,
    override val commentValue: Int
) : BoardPost
