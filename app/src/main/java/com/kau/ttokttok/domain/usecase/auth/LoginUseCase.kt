package com.kau.ttokttok.domain.usecase.auth

import com.kau.ttokttok._core.network.auth.*
import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenProvider: TokenProvider,
    private val userProvider: UserProvider
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.login(email, password)
            .map { result ->
                tokenProvider.update(
                    jwt = result.accessToken,
                    refresh = result.refreshToken
                )

                userProvider.setBuildingNumber(result.buildingNumber)
                userProvider.setUnitNumber(result.unitNumber)
            }
    }
}