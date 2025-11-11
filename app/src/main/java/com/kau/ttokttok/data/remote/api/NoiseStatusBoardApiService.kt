package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.noiseboard.req.*
import com.kau.ttokttok.data.remote.dto.noiseboard.res.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
    ): ApiResponse<CancelVoteRes>

    @DELETE("api/comment/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") id: Long
    ): ApiResponse<DeleteCommentRes>
}