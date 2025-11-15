package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.data.remote.dto.community.res.*
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail

interface CommunityRepository {
    suspend fun createPost(title: String, content: String) : Result<String>
    suspend fun getPosts(): Result<List<CommunityBoard>>
    suspend fun getPostDetail(id: Long): Result<CommunityBoardDetail>
}