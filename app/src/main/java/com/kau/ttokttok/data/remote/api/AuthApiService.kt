package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.auth.req.LoginReq
import com.kau.ttokttok.data.remote.dto.auth.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.auth.req.RequestTempPasswordReq
import com.kau.ttokttok.data.remote.dto.auth.req.VerifyEmailReq
import com.kau.ttokttok.data.remote.dto.auth.res.LoginRes
import com.kau.ttokttok.data.remote.dto.auth.res.RegisterRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {
    @Headers("No-Auth: true")
    @POST("api/users/login")
    suspend fun login(@Body req: LoginReq): ApiResponse<LoginRes>

    @Headers("No-Auth: true")
    @POST("api/users/join")
    suspend fun register(@Body req: RegisterReq): ApiResponse<RegisterRes>

    @Headers("No-Auth: true")
    @POST("api/users/issue-temporary-password")
    suspend fun requestTemporaryPassword(@Body req: RequestTempPasswordReq): ApiResponse<String>

    @Headers("api/users/verify-email")
    @GET("api/users/verify-email")
    suspend fun verifyEmail(@Body req: VerifyEmailReq): ApiResponse<String>
}