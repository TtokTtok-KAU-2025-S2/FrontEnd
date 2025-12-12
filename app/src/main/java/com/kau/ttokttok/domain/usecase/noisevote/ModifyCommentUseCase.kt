package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.data.remote.dto.noiseboard.res.ModifyCommentRes
import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModifyCommentUseCase @Inject constructor(
    private val repository: NoiseVoteRepository
) {
    suspend operator fun invoke(id: Long, content: String): Result<ModifyCommentRes> {
        return repository.modifyComment(id, content)
    }
}