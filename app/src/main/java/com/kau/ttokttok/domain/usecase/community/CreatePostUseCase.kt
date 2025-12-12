package com.kau.ttokttok.domain.usecase.community

import com.kau.ttokttok.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatePostUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(title: String, content: String, imageUri: String?): Result<String> = runCatching {
        repository.createPost(title, content, imageUri)
    }
}