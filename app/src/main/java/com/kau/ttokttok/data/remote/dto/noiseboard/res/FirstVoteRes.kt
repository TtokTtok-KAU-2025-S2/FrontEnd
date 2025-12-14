package com.kau.ttokttok.data.remote.dto.noiseboard.res

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.domain.usecase.noisevote.PostVoteResult

data class FirstVoteRes(
    val voteId: Long,
    val voteType: NoiseVoteType,
    val createdAt: String
) {
    fun toResult(): PostVoteResult {
        return PostVoteResult(
            voteType = voteType
        )
    }
}
