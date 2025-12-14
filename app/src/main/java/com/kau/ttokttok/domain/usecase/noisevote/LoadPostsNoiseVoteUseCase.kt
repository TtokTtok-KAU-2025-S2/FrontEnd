package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadPostsNoiseVoteUseCase @Inject constructor(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(): Result<List<NoiseVoteBoard>> = repository.getPosts()
}