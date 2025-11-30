package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.ai.req.GetAICategoryReq
import com.kau.ttokttok.data.remote.dto.ai.res.GetAICategoryRes
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * AI 분석 API Service
 */
interface AIApiService {
    /**
     * AI 카테고리 분석
     *
     * 녹음 파일을 AI가 분석하여 소음 카테고리 자동 판단
     * - 음성 인식 (transcript)
     * - 카테고리 분류 (category)
     * - 판단 이유 (reason)
     * - 데시벨 정보 (dbMax, dbAvg)
     *
     * @param req recordId를 포함한 요청 데이터
     * @return AI 분석 결과
     */
    @POST("noise/ai/category")
    suspend fun getAICategory(
        @Body req: GetAICategoryReq
    ): ApiResponse<GetAICategoryRes>
}

