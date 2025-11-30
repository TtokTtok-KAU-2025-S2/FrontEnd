package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.data.remote.dto.noiseboard.res.FirstVoteRes
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Singleton

@Singleton
class PostVoteUseCase(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(id: Long, voteType: NoiseVoteType) : Result<FirstVoteRes> {
        return repository.firstVote(id, voteType)
    }

}