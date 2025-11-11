package com.kau.ttokttok.data.remote.dto.noiseboard.res

import com.kau.ttokttok.domain.model.board.VoteType
import java.time.LocalDateTime

data class FirstVoteRes(
    val voteId: Long,
    val voteType: VoteType,
    val createdAt: LocalDateTime
)
