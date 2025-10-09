package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok.data.local.storage.AuthStorage
import com.kau.ttokttok.data.remote.api.AuthApiService
import com.kau.ttokttok.data.remote.dto.req.LoginReq
import com.kau.ttokttok.domain.model.User
import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val storage: AuthStorage
) : AuthRepository {
    // TODO: 로그인 로직_수정
    override suspend fun login(id: String, pw: String): Result<User> {
        throw Exception("수정하기")
    }
}