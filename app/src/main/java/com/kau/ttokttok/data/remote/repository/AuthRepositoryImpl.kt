package com.kau.ttokttok.data.remote.repository

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
    override suspend fun login(email: String, password: String): LoginRes {
        val req = LoginReq(
            email = email,
            password = password
        )

        return when (val response = safeApiCall { api.login(req) }) {
            is NetworkResult.Success -> {
                response.data
            }

            is NetworkResult.Error -> {
                throw Throwable(response.message)
            }
        }
    }

    override suspend fun register(
        aptId: Long,
        email: String,
        password: String,
        buildingNumber: Int,
        unitNumber: Int
    ): RegisterRes {
        val req = RegisterReq(
            aptId = aptId,
            email = email,
            password = password,
            buildingNumber = buildingNumber,
            unitNumber = unitNumber
        )

        return when (val response = safeApiCall { api.register(req) }) {
            is NetworkResult.Success -> {
                response.data
            }

            is NetworkResult.Error -> {
                throw Throwable(response.message ?: "회원가입에 실패했습니다.")
            }
        }
    }

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