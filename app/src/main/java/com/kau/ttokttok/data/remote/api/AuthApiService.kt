package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.auth.req.*
import com.kau.ttokttok.data.remote.dto.auth.res.*
import retrofit2.http.Body
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
}