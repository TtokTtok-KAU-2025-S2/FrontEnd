package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok.data.remote.dto.req.LoginReq
import com.kau.ttokttok.data.remote.dto.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.res.CommonRes
import retrofit2.http.Body
import retrofit2.http.POST

// TODO: BE연동_바꾸기
interface AuthApiService {
    @POST("/login")
    // 제네릭 타입 -> 토큰 모델링 이후 수정
    suspend fun login(@Body req: LoginReq): CommonRes<Unit>

    @POST("/register")
    // 제네릭 타입 -> 토큰 모델링 이후 수정
    suspend fun register(@Body req: RegisterReq): CommonRes<Unit>
}