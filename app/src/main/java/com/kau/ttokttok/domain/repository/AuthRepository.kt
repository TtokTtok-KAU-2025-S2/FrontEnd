package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.usecase.auth.LoginResult

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResult>
    suspend fun register(
        aptId: Long,
        email: String,
        password: String,
        buildingNumber: Int,
        unitNumber: Int
    ): Result<Unit>
    suspend fun requestTempPassword(email: String): Result<Unit>
}