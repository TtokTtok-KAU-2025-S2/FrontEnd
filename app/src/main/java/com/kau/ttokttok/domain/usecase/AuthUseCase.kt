package com.kau.ttokttok.domain.usecase

import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok._core.network.auth.UserProvider
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenProvider: TokenProvider,
    private val userProvider: UserProvider
) {
    suspend fun changePassword(password: String): NetworkResult<String> {
        return NetworkResult.Success("비밀번호 변경 성공")
    }
    suspend fun changeAddress(address: String): NetworkResult<String> {
        return NetworkResult.Success("거주지 변경 성공")
    }
}