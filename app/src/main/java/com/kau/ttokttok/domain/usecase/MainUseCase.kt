package com.kau.ttokttok.domain.usecase

import com.kau.ttokttok._core.network.auth.UserProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainUseCase @Inject constructor(
    private val userProvider: UserProvider
) {
    fun getBuildingNumber(): Int {
        return userProvider.getBuildingNumberOrNull() ?: 0
    }

    fun getUnitNumber(): Int {
        return userProvider.getUnitNumberOrNull() ?: 0
    }
}