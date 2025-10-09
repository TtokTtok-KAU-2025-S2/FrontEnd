package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.data.remote.dto.res.CommonRes
import com.kau.ttokttok.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}