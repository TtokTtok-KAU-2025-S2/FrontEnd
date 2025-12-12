package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CancelVoteUseCase @Inject constructor(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(id: Long): Result<String> {
        return repository.cancelVote(id)
    }
}