package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.preconsideration.req.*
import com.kau.ttokttok.data.remote.dto.preconsideration.res.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PreNoticeApiService {
    @GET("api/prenotices")
    suspend fun getPosts(): ApiResponse<GetPostsPreConsiderationRes>

    @GET("api/prenotice/{preNoticeId}")
    suspend fun getPostDetail(
        @Path("preNoticeId") id: Long
    ): ApiResponse<GetPostDetailPreConsiderationRes>

    @POST("api/prenotices")
    suspend fun createPost(@Body req: CreatePostPreConsiderationReq): ApiResponse<CreatePostPreConsiderationRes>

    @PUT("api/prenotices/{preNoticeId}")
    suspend fun modifyPost(
        @Path("preNoticeId") id: Long,
        @Body req: ModifyPostPreConsiderationReq
    ): ApiResponse<ModifyPostPreConsiderationRes>

    @DELETE("api/prenotices/{preNoticeId}")
    suspend fun deletePost(
        @Path("preNoticeId") id: Long
    ): ApiResponse<DeletePostPreConsiderationRes>
}