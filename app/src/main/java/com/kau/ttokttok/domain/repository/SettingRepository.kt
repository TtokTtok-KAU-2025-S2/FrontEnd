package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult

interface SettingRepository {
    suspend fun getNotice(): NetworkResult<String>
    suspend fun getTerms(): NetworkResult<String>
    suspend fun getPrivacyPolicy(): NetworkResult<String>
}