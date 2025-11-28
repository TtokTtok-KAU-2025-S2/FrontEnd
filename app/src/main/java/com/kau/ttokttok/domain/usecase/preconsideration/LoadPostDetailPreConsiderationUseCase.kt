package com.kau.ttokttok.domain.usecase.preconsideration

import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadPostDetailPreConsiderationUseCase @Inject constructor(
    private val repository: PreConsiderationRepository
){
    suspend operator fun invoke(id: Long): Result<PreConsiderationBoardDetail> {
        return repository.getPostDetail(id)
    }
}