package com.kau.ttokttok.domain.usecase.auth

import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        aptId: Long,
        email: String,
        password: String,
        buildingNumber: Int,
        unitNumber: Int
    ): Result<Unit> = runCatching {
        repository.register(
            aptId = aptId,
            email = email,
            password = password,
            buildingNumber = buildingNumber,
            unitNumber = unitNumber
        )
    }
}