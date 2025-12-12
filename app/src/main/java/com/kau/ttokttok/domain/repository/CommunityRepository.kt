package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail

interface CommunityRepository {
    suspend fun createPost(title: String, content: String, imageUri: String?) : String
    suspend fun getPosts(): List<CommunityBoard>
    suspend fun getPostDetail(id: Long): CommunityBoardDetail
}