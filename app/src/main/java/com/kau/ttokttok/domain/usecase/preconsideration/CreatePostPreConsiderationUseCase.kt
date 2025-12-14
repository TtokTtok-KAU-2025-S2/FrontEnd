package com.kau.ttokttok.domain.usecase.preconsideration

import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatePostPreConsiderationUseCase @Inject constructor(
    private val repository: PreConsiderationRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): Result<Unit> {
         return repository.createPost(
            title = title,
            content = content,
            noticeDate = noticeDate,
            noticeTime = noticeTime,
            noticeReason = noticeReason
        )
    }
}