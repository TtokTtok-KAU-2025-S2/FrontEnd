package com.kau.ttokttok.domain.model

import java.util.Date

/**
 * 소음 일기 모델
 */
data class NoiseLog(
    val id: String? = null,
    val noiseType: String, // "발걸음", "망치질" 등
    val maxDecibel: Double,
    val avgDecibel: Double,
    val memo: String,
    val measuredAt: Date,
    val hasReport: Boolean = false
)
