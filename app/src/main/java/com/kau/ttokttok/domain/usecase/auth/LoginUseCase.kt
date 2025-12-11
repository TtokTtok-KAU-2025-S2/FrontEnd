package com.kau.ttokttok.domain.usecase.auth

import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok._core.network.auth.UserProvider
import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenProvider: TokenProvider,
    private val userProvider: UserProvider
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> = runCatching {
        val res = repository.login(email, password)

        tokenProvider.update(
            jwt = res.accessToken,
            refresh = res.refreshToken
        )

        userProvider.setBuildingNumber(
            buildingNumber = res.userDetailDto.buildingNumber
        )

        userProvider.setUnitNumber(
            unitNumber = res.userDetailDto.unitNumber
        )
    }
}