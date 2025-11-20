package com.kau.ttokttok.domain.usecase

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.domain.repository.SettingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingUseCase @Inject constructor(
    private val repository: SettingRepository
) {
    suspend fun getNotice(): NetworkResult<String> {
        return repository.getNotice()
    }
    suspend fun getTerms(): NetworkResult<String> {
        return repository.getTerms()
    }
    suspend fun getPrivacyPolicy(): NetworkResult<String> {
        return repository.getPrivacyPolicy()
    }
}
