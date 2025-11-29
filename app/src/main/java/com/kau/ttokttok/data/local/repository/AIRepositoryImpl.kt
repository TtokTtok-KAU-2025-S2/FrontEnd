package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.AIApiService
import com.kau.ttokttok.data.remote.dto.ai.req.GetAICategoryReq
import com.kau.ttokttok.data.remote.dto.ai.res.GetAICategoryRes
import com.kau.ttokttok.domain.repository.AIRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI 분석 Repository 구현체
 */
@Singleton
class AIRepositoryImpl @Inject constructor(
    private val api: AIApiService
) : AIRepository {
    /**
     * AI 카테고리 분석
     *
     * @param recordId 녹음 파일 ID
     * @return AI 분석 결과
     */
    override suspend fun getAICategory(recordId: Long): NetworkResult<GetAICategoryRes> =
        safeApiCall {
            api.getAICategory(GetAICategoryReq(recordId))
        }
}

