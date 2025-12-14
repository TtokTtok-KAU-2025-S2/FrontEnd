package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.noiseboard.req.*
import com.kau.ttokttok.data.remote.dto.noiseboard.res.*
import retrofit2.http.*

interface NoiseStatusBoardApiService {
    @GET("api/noise-reports")
    suspend fun getPosts(): ApiResponse<GetPostsNoiseBoardRes>

    @GET("api/noise-reports/{reportId}")
    suspend fun getPostDetail(
        @Path("reportId") id: Long
    ): ApiResponse<GetPostDetailNoiseBoardRes>

    @POST("api/noise-reports/{reportId}/votes")
    suspend fun firstVote(
        @Path("reportId") id: Long,
        @Body req: FirstVoteReq
    ): ApiResponse<FirstVoteRes>

    @POST("api/noise-reports/{reportId}/comments")
    suspend fun postComment(
        @Path("reportId") id: Long,
        @Body req: PostCommentReq
    ): ApiResponse<PostCommentRes>

    @PUT("api/comments/{commentId}")
    suspend fun modifyComment(
        @Path("commentId") id: Long,
        @Body req: ModifyCommentReq
    ): ApiResponse<ModifyCommentRes>

    @DELETE("api/noise-reports/{reportId}/votes")
    suspend fun cancelVote(
        @Path("reportId") id: Long
    ): ApiResponse<String>

    @DELETE("api/comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") id: Long
    ): ApiResponse<String>
}