package com.kau.ttokttok.data.remote.dto.noiseboard.res

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import java.time.LocalDateTime

data class FirstVoteRes(
    val voteId: Long,
    val noiseVoteType: NoiseVoteType,
    val createdAt: LocalDateTime
)
