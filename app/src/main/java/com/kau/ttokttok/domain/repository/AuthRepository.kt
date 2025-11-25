package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.auth.req.LoginReq
import com.kau.ttokttok.data.remote.dto.auth.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.auth.req.RequestTempPasswordReq
import com.kau.ttokttok.data.remote.dto.auth.res.LoginRes
import com.kau.ttokttok.data.remote.dto.auth.res.RegisterRes

interface AuthRepository {
    suspend fun login(loginReq: LoginReq): NetworkResult<LoginRes>
    suspend fun register(registerReq: RegisterReq): NetworkResult<RegisterRes>
    suspend fun requestTempPassword(email: String): String
}