package com.kau.ttokttok.data.remote.dto.ai.res

/**
 * AI 카테고리 분석 응답 (POST /noise/ai/category)
 *
 * AI가 분석한 소음 카테고리 및 상세 정보
 *
 * @param category 소음 카테고리 (FOOTSTEPS, HAMMERING, FURNITURE, MUSIC, VOICE, PET, APPLIANCE, DOOR, WATER, CONSTRUCTION, EXERCISE, UNKNOWN)
 * @param transcript 음성 인식 결과 (예: "음악 소리")
 * @param reason AI가 해당 카테고리로 판단한 이유
 * @param createdAt 분석 완료 시각
 * @param duration 녹음 파일 길이 (초)
 * @param dbMax 최대 데시벨
 * @param dbAvg 평균 데시벨
 */
data class GetAICategoryRes(
    val category: String,
    val transcript: String,
    val reason: String,
    val createdAt: String,
    val duration: Int,
    val dbMax: Double,
    val dbAvg: Double
)

/**
 * UI에서 사용할 AI 분석 결과 데이터
 */
data class AICategoryResult(
    val category: String,
    val categoryKorean: String,
    val transcript: String,
    val reason: String,
    val duration: Int,
    val dbMax: Double,
    val dbAvg: Double
)

/**
 * 카테고리 영문 → 한글 변환
 */
fun String.toCategoryKorean(): String = when (this) {
    "FOOTSTEPS" -> "발걸음"
    "HAMMERING" -> "망치질"
    "FURNITURE" -> "가구 끄는 소리"
    "MUSIC" -> "음악 소리"
    "VOICE" -> "고성방가"
    "PET" -> "반려동물 소리"
    "APPLIANCE" -> "가전제품 소리"
    "DOOR" -> "문 여닫는 소리"
    "WATER" -> "물 소리"
    "CONSTRUCTION" -> "인테리어 공사"
    "EXERCISE" -> "운동 기구 소리"
    "UNKNOWN" -> "기타"
    else -> "알 수 없음"
}

/**
 * Response를 UI 데이터로 변환
 */
fun GetAICategoryRes.toAICategoryResult(): AICategoryResult {
    return AICategoryResult(
        category = category,
        categoryKorean = category.toCategoryKorean(),
        transcript = transcript,
        reason = reason,
        duration = duration,
        dbMax = dbMax,
        dbAvg = dbAvg
    )
}

