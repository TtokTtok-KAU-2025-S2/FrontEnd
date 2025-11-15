package com.kau.ttokttok.domain.usecase.community

import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.repository.CommunityRepository

class LoadPostsUseCase(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(): Result<List<CommunityBoard>> = runCatching {
        repository.getPosts()
    }
}