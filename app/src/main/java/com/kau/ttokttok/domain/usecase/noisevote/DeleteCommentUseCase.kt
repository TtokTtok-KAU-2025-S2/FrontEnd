package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.data.remote.dto.noiseboard.res.DeleteCommentRes
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Singleton

@Singleton
class DeleteCommentUseCase(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(id: Long): Result<DeleteCommentRes> {
        return repository.deleteComment(id)
    }

}