package com.kau.ttokttok.domain.usecase.preconsideration

import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModifyPostDetailPreConsiderationUseCase @Inject constructor(
    private val repository: PreConsiderationRepository
) {
    suspend operator fun invoke(
        id: Long,
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): Result<Unit> = runCatching {
        repository.modifyPost(
            id = id,
            title = title,
            content = content,
            noticeDate = noticeDate,
            noticeTime = noticeTime,
            noticeReason = noticeReason
        )
    }
}