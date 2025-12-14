package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostVoteUseCase @Inject constructor(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(id: Long, voteType: NoiseVoteType) : Result<PostVoteResult> = repository.firstVote(id, voteType)
}