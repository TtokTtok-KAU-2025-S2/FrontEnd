package com.kau.ttokttok.domain.usecase.community

import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPostDetailUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(id: Long): Result<CommunityBoardDetail> = repository.getPostDetail(id)
}