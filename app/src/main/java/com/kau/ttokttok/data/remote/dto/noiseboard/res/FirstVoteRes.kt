package com.kau.ttokttok.data.remote.dto.noiseboard.res

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType

data class FirstVoteRes(
    val voteId: Long,
    val voteType: NoiseVoteType,
    val createdAt: String
)
