package com.kau.ttokttok.domain.usecase

import android.util.Patterns
import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok._core.network.auth.UserProvider
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.auth.req.LoginReq
import com.kau.ttokttok.data.remote.dto.auth.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.auth.res.LoginRes
import com.kau.ttokttok.data.remote.dto.auth.res.RegisterRes
import com.kau.ttokttok.domain.repository.AuthRepository
import okhttp3.Address
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenProvider: TokenProvider,
    private val userProvider: UserProvider
) {
    suspend fun login(email: String, password: String): NetworkResult<LoginRes> {

        val result = repository.login(LoginReq(email, password))

        if (result is NetworkResult.Success) {
            tokenProvider.update(
                jwt = result.data.accessToken
                // TODO: Refresh Token 구현 시
                // refreshToken = result.data.refreshToken
            )

            userProvider.setBuildingNumber(
                buildingNumber = result.data.userDetailDto.buildingNumber
            )

            userProvider.setUnitNumber(
                unitNumber = result.data.userDetailDto.unitNumber
            )
        }

        return result
    }

    suspend fun register(
        aptId: Long,
        email: String,
        password: String,
        buildingNumber: Int,
        unitNumber: Int
    ): NetworkResult<RegisterRes> {

        val result = repository.register(
            RegisterReq(
                aptId = aptId,
                email = email,
                password = password,
                buildingNumber = buildingNumber,
                unitNumber = unitNumber
            )
        )

        if (result is NetworkResult.Success) {

        }

        return result
    }
    suspend fun logout() {
        tokenProvider.clear()
        userProvider.clear()
    }

    suspend fun changePassword(password: String): NetworkResult<String> {
        return NetworkResult.Success("비밀번호 변경 성공")
    }
    suspend fun changeAddress(address: String): NetworkResult<String> {
        return NetworkResult.Success("거주지 변경 성공")
    }
}