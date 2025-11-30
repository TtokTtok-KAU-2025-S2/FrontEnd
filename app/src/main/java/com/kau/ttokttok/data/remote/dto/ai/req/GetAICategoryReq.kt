package com.kau.ttokttok.data.remote.dto.ai.req

import com.squareup.moshi.Json

/**
 * AI 카테고리 분석 요청 (POST /noise/ai/category)
 *
 * 녹음 파일을 AI가 분석하여 소음 카테고리 자동 판단
 *
 * @param recordId 음성파일 고유 번호 (녹음 파일 ID)
 */
data class GetAICategoryReq(
    @Json(name = "recordId")
    val recordId: Long
)

