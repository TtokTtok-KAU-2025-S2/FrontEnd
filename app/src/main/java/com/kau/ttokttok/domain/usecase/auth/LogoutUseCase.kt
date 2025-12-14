package com.kau.ttokttok.domain.usecase.auth

import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok._core.network.auth.UserProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val userProvider: UserProvider
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        tokenProvider.clear()
        userProvider.clear()
    }
}