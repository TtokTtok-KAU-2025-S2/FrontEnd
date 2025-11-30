package com.kau.ttokttok.domain.usecase.noisevote

import com.kau.ttokttok.data.remote.dto.noiseboard.res.PostCommentRes
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Singleton

@Singleton
class AddCommentUseCase(
    private val repository: NoiseVoteRepository
){
    suspend operator fun invoke(id: Long, content: String): Result<PostCommentRes> {
        return repository.postComment(id, content)
    }

}