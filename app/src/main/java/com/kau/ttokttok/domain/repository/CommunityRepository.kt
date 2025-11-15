package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.community.req.*
import com.kau.ttokttok.data.remote.dto.community.res.*
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail

interface CommunityRepository {
    suspend fun createPost(req: CreatePostCommunityReq) : NetworkResult<CreatePostCommunityRes>
    suspend fun getPosts(): Result<List<CommunityBoard>>
    suspend fun getPostDetail(id: Long): Result<CommunityBoardDetail>
}