package com.kau.ttokttok.domain.usecase.auth

import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestTempPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> = repository.requestTempPassword(email)
}