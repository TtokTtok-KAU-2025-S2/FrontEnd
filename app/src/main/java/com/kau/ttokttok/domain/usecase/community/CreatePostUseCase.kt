package com.kau.ttokttok.domain.usecase.community

import com.kau.ttokttok.domain.repository.CommunityRepository
import kotlinx.coroutines.runBlocking


class CreatePostUseCase(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(title: String, content: String): Result<String>  {
        return repository.createPost(title, content)
    }
}