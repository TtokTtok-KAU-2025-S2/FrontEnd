package com.kau.ttokttok.domain.usecase.community

import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.repository.CommunityRepository
import kotlinx.coroutines.runBlocking


class GetPostDetailUseCase(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(id: Long): Result<CommunityBoardDetail> = runCatching {
        repository.getPostDetail(id)
    }
}