package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail

interface CommunityRepository {
    suspend fun createPost(title: String, content: String, imageUri: String?): Result<Unit>
    suspend fun getPosts(): Result<List<CommunityBoard>>
    suspend fun getPostDetail(id: Long): Result<CommunityBoardDetail>
}