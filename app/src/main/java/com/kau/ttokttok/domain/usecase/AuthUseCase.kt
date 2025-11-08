package com.kau.ttokttok.domain.usecase

import android.util.Patterns
import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.auth.req.LoginReq
import com.kau.ttokttok.data.remote.dto.auth.req.RegisterReq
import com.kau.ttokttok.data.remote.dto.auth.res.LoginRes
import com.kau.ttokttok.data.remote.dto.auth.res.RegisterRes
import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenProvider: TokenProvider
) {
    suspend fun login(email: String, password: String): NetworkResult<LoginRes> {
        validateEmail(email)
        validatePassword(password)

        val result = repository.login(LoginReq(email, password))

        if (result is NetworkResult.Success) {
            tokenProvider.update(
                jwt = result.data.accessToken
                // TODO: Refresh Token 구현 시
                // refreshToken = result.data.refreshToken
            )
        }

        return result
    }

    suspend fun register(
        email: String,
        password: String,
        buildingNumber: Int,
        unitNumber: Int
    ): NetworkResult<RegisterRes> {
        validateEmail(email)
        validatePassword(password)

        val result = repository.register(
            RegisterReq(
                email = email,
                password = password,
                buildingNumber = buildingNumber,
                unitNumber = unitNumber
            )
        )

        if (result is NetworkResult.Success) {
            tokenProvider.update(
                jwt = result.data.accessToken
                // TODO: Refresh Token 구현 시
                // refreshToken = result.data.refreshToken
            )
        }

        return result
    }

    private fun validateEmail(email: String) {
        require(email.isNotBlank()) {"이메일을 입력하세요."}
        require(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {"올바른 이메일 형식이 아닙니다."}
    }

    private fun validatePassword(password: String) {
        require(password.isNotBlank()) {"비밀번호를 입력하세요."}

        require(password.length >= 8) {"비밀번호는 8자 이상입니다."}
        require(password.first().isUpperCase()) {"비밀번호는 대문자로 시작해야 합니다."}
        require(password.any { it.isDigit() }) {"비밀번호에는 숫자가 하나 이상 포함되어야 합니다."}

        val specialChars = setOf('!', '@', '#', '$', '%', '^', '&', '*', '-', '_', '+', '=', '?')
        require(password.any { it in specialChars }) { "비밀번호에는 특수문자가 하나 이상 포함되어야 합니다." }
    }

}