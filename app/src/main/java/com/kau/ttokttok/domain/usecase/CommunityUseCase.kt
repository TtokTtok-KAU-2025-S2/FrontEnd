package com.kau.ttokttok.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityUseCase @Inject constructor(
    // private val repository: CommunityRepository
) {
    suspend fun loadPosts() {
        // val result = repository.loadPosts(req)
        // return result
    }

    suspend fun loadPostDetail() {
        // val result = repository.loadPostDetail(req)
        // return result
    }

    suspend fun createPost(title: String, content: String) {
        // 이미지는 일단 ""
        // val result = repository.createPost(Req)
        // return result
    }
}