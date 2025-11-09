package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.community.req.CreatePostCommunityReq
import com.kau.ttokttok.data.remote.dto.community.res.CreatePostCommunityRes
import com.kau.ttokttok.data.remote.dto.community.res.GetPostDetailRes
import com.kau.ttokttok.data.remote.dto.community.res.GetPostsRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommunityApiService {
    @POST("api/notice")
    suspend fun createPost(@Body req: CreatePostCommunityReq): ApiResponse<CreatePostCommunityRes>

    // TODO: 아래 메소드들은 BE 준비중
    @GET("api/notice")
    suspend fun getPosts(): ApiResponse<GetPostsRes>

    @GET("api/notice/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): ApiResponse<GetPostDetailRes>
}