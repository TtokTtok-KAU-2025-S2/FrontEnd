package com.kau.ttokttok.data.remote.dto.noiseboard.req

import com.kau.ttokttok.domain.model.board.noisevote.VoteType

data class FirstVoteReq(
    val voteType: VoteType
)
