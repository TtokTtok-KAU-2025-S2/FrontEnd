package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.AuthApiService
import com.kau.ttokttok.data.remote.dto.auth.req.LoginReq
import com.kau.ttokttok.data.remote.dto.auth.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.auth.req.RequestTempPasswordReq
import com.kau.ttokttok.data.remote.dto.auth.res.LoginRes
import com.kau.ttokttok.data.remote.dto.auth.res.RegisterRes
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

    override suspend fun requestTempPassword(email: String): String {
        val req = RequestTempPasswordReq(
            email = email
        )

        return when (val response = safeApiCall { api.requestTemporaryPassword(req) }) {
            is NetworkResult.Success -> {
                response.data
            }

            is NetworkResult.Error -> {
                throw Throwable(response.message ?: "ERROR")
            }
        }
    }
}