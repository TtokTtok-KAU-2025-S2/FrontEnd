package com.kau.ttokttok.data.remote.dto.noiserecord.res

import com.kau.ttokttok.domain.model.NoiseLog
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

data class CreateNoiseRecordRes(
    val id: Long,
    val userId: Long,
    val duration: Int,
    val dbHigh: Double,
    val dbAvg: Double,
    val category: String,
    val grade: String,
    val description: String?,  // 사용자가 작성한 메모 (소음일기용)
    val summary: String?,      // AI가 자동 생성한 요약 (소음현황판용, 전송 시 생성됨)
    // 서버에서 LocalDateTime 문자열을 내려주므로, Moshi 커스텀 어댑터 없이 String으로 수신
    val occuredAt: String,
    val updateAt: String
) {
    /**
     * Response String 날짜를 Domain 모델로 변환
     * 소음일기에서는 description 우선 표시
     */
    fun toDomain(): NoiseLog {
        val measuredDate = parseStringToDate(occuredAt)
        val memoText = description ?: ""

        return NoiseLog(
            id = id.toString(),
            noiseType = category,
            maxDecibel = dbHigh,
            avgDecibel = dbAvg,
            memo = memoText,
            measuredAt = measuredDate,
            duration = duration.toLong(),
            hasReport = false
        )
    }

    private fun parseStringToDate(dateString: String): Date {
        val localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return Date.from(localDateTime.atZone(ZoneOffset.systemDefault()).toInstant())
    }
}
