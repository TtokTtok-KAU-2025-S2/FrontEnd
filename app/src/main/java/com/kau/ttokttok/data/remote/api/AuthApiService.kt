package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.req.*
import com.kau.ttokttok.data.remote.dto.res.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("user/login")
    suspend fun login(@Body req: LoginReq): ApiResponse<LoginRes>

    @POST("user/register")
    suspend fun register(@Body req: RegisterReq): ApiResponse<RegisterRes>
}