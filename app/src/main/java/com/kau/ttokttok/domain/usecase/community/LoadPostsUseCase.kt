package com.kau.ttokttok.domain.usecase.community

import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadPostsUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(): Result<List<CommunityBoard>> = repository.getPosts()
}