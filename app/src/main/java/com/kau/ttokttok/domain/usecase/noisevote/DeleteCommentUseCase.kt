package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCommentUseCase @Inject constructor(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteComment(id)
}