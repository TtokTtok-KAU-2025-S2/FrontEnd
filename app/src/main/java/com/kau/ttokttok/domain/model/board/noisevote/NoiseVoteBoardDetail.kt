package com.kau.ttokttok.domain.model.board.noisevote

import com.kau.ttokttok.domain.model.board.Comment
import java.time.LocalDateTime

data class NoiseVoteBoardDetail(
    val title: String,
    val buildingNumber: Int,
    val reportedAt: LocalDateTime,
    val maxDb: Int,
    val avgDb: Int,
    val category: String,
    val voteCount: Map<NoiseVoteType, Int>,
    val comments: List<Comment>
)