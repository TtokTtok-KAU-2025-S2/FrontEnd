package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.req.*
import com.kau.ttokttok.data.remote.dto.res.*

interface AuthRepository {
    suspend fun login(loginReq: LoginReq): NetworkResult<LoginRes>
    suspend fun register(registerReq: RegisterReq): NetworkResult<RegisterRes>
}