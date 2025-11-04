package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.auth.req.LoginReq
import com.kau.ttokttok.data.remote.dto.auth.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.auth.res.LoginRes
import com.kau.ttokttok.data.remote.dto.auth.res.RegisterRes
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("user/login")
    suspend fun login(@Body req: LoginReq): ApiResponse<LoginRes>

    @POST("user/register")
    suspend fun register(@Body req: RegisterReq): ApiResponse<RegisterRes>
}