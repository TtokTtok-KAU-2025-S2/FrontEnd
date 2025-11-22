package com.kau.ttokttok.data.remote.dto.noiserecord.req

import com.kau.ttokttok.domain.model.NoiseLog
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

data class ModifyNoiseRecordReq(
    val category: String,
    val occuredAt: String,
    val noiseGrade: String,
    val dbHigh: Double,
    val dbAvg: Double,
    val summary: String?
) {
    companion object {
        /**
         * Domain 모델로부터 Request 생성 (Date → String 변환)
         */
        fun from(noiseLog: NoiseLog): ModifyNoiseRecordReq {
            return ModifyNoiseRecordReq(
                category = mapCategory(noiseLog.noiseType),
                occuredAt = formatDateToString(noiseLog.measuredAt),
                noiseGrade = mapGrade(noiseLog.avgDecibel),
                dbHigh = noiseLog.maxDecibel,
                dbAvg = noiseLog.avgDecibel,
                summary = noiseLog.memo
            )
        }

        private fun formatDateToString(date: Date): String {
            return Instant.ofEpochMilli(date.time)
                .atOffset(ZoneOffset.systemDefault().rules.getOffset(Instant.ofEpochMilli(date.time)))
                .toLocalDateTime()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }

        private fun mapCategory(noiseType: String): String = when (noiseType.uppercase()) {
            "FOOTSTEPS", "발걸음" -> "FOOTSTEPS"
            "HAMMERING", "망치질" -> "HAMMERING"
            "FURNITURE", "가구 끄는 소리", "가구" -> "FURNITURE"
            "MUSIC", "음악 소리", "음악" -> "MUSIC"
            "KIDS", "아이들 뛰는 소리" -> "UNKNOWN"
            "VACUUM", "청소기 소리" -> "UNKNOWN"
            else -> "UNKNOWN"
        }

        private fun mapGrade(avg: Double): String = when {
            avg >= 65 -> "LOUD"
            avg >= 45 -> "NORMAL"
            else -> "QUIET"
        }
    }
}

