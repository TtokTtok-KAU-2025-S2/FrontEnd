package com.kau.ttokttok.domain.model.board.noisevote

import java.time.LocalDateTime

data class NoiseVoteBoard(
    val id: Long,
    val authorLocation: String,
    val title: String,
    val reportedAt: LocalDateTime,
    val comment: Int,
    val category: String,
    val currentVote: Int,
    val totalVote: Int
)