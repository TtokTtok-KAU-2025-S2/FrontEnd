package com.kau.ttokttok.data.remote.dto.noiserecord.res

import com.kau.ttokttok.domain.model.NoiseLog
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

data class ModifyNoiseRecordRes(
    val noiseId: Long,
    // TODO: 추후 Enum으로 변경
    val category: String,
    // 서버에서 LocalDateTime 문자열을 내려주므로 문자열로 수신
    val occuredAt: String,
    // TODO: 추후 Enum으로 변경
    val noiseGrade: String,
    val dbHigh: Double,
    val dbAvg: Double,
    val summary: String,
    val updatedAt: String
) {
    /**
     * Response String 날짜를 Domain 모델로 변환
     */
    fun toDomain(): NoiseLog {
        val measuredDate = parseStringToDate(occuredAt)

        return NoiseLog(
            id = noiseId.toString(),
            noiseType = category,
            maxDecibel = dbHigh,
            avgDecibel = dbAvg,
            memo = summary,
            measuredAt = measuredDate,
            hasReport = false
        )
    }

    private fun parseStringToDate(dateString: String): Date {
        val localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return Date.from(localDateTime.atZone(ZoneOffset.systemDefault()).toInstant())
    }
}
