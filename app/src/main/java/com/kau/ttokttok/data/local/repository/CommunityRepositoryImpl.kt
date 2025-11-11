package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.CommunityApiService
import com.kau.ttokttok.data.remote.dto.community.req.CreatePostCommunityReq
import com.kau.ttokttok.data.remote.dto.community.res.CreatePostCommunityRes
import com.kau.ttokttok.data.remote.dto.community.res.GetPostDetailRes
import com.kau.ttokttok.data.remote.dto.community.res.GetPostsRes
import com.kau.ttokttok.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val api: CommunityApiService
) : CommunityRepository {
    override suspend fun createPost(req: CreatePostCommunityReq): NetworkResult<CreatePostCommunityRes> =
        safeApiCall { api.createPost(req) }

//    override suspend fun getPosts(): NetworkResult<GetPostsRes> =
//        safeApiCall { api.getPosts() }
//
//    override suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailRes> =
//        safeApiCall { api.getPostDetail(id) }
}