package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.community.req.CreatePostCommunityReq
import com.kau.ttokttok.data.remote.dto.community.res.CreatePostCommunityRes
import com.kau.ttokttok.data.remote.dto.community.res.GetPostDetailRes
import com.kau.ttokttok.data.remote.dto.community.res.GetPostsRes

interface CommunityRepository {
    suspend fun createPost(req: CreatePostCommunityReq) : NetworkResult<CreatePostCommunityRes>
    suspend fun getPosts(): NetworkResult<GetPostsRes>
    suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailRes>
}