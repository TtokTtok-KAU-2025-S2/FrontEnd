package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Singleton

@Singleton
class LoadPostsNoiseVoteUseCase(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(): Result<List<NoiseVoteBoard>> {
        return repository.getPosts()
    }
}