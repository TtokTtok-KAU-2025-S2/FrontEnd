package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.community.res.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CommunityApiService {
    @Multipart
    @POST("api/notice")
    suspend fun createPost(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part noticePicture: MultipartBody.Part?
    ): ApiResponse<CreatePostCommunityRes>

    @GET("api/notice")
    suspend fun getPosts(): ApiResponse<GetPostsRes>

    @GET("api/notice/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): ApiResponse<GetPostDetailRes>
}