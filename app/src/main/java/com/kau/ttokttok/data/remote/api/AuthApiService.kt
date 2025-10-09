package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok.data.remote.dto.req.LoginReq
import com.kau.ttokttok.data.remote.dto.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.res.CommonRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

// TODO: BE연동_바꾸기
interface AuthApiService {
    @POST("/login")
    suspend fun login(@Body req: LoginReq): Response<CommonRes<Unit>>

    @POST("/register")
    suspend fun register(@Body req: RegisterReq): Response<CommonRes<Unit>>

    @DELETE("/user")
    suspend fun logout(): Response<CommonRes<Unit>>
}