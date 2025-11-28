package com.kau.ttokttok.data.remote.dto.noiseboard.req

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType

data class FirstVoteReq(
    val noiseVoteType: NoiseVoteType
)
