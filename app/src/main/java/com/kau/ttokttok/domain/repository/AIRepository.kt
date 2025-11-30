package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.ai.res.GetAICategoryRes

/**
 * AI 분석 Repository
 */
interface AIRepository {
    /**
     * AI 카테고리 분석
     *
     * @param recordId 녹음 파일 ID
     * @return AI 분석 결과
     */
    suspend fun getAICategory(recordId: Long): NetworkResult<GetAICategoryRes>
}

