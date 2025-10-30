package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.AuthApiService
import com.kau.ttokttok.data.remote.dto.req.LoginReq
import com.kau.ttokttok.data.remote.dto.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.res.LoginRes
import com.kau.ttokttok.data.remote.dto.res.RegisterRes
import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {
    override suspend fun login(loginReq: LoginReq): NetworkResult<LoginRes> =
        safeApiCall { api.login(loginReq) }

    override suspend fun register(registerReq: RegisterReq): NetworkResult<RegisterRes> =
        safeApiCall { api.register(registerReq) }
}