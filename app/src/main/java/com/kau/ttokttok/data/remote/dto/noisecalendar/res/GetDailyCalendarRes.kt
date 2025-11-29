package com.kau.ttokttok.data.remote.dto.noisecalendar.res

import com.kau.ttokttok.domain.model.NoiseLog
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

data class GetDailyCalendarRes(
    val userId: Long,
    val year: Int,
    val month: Int,
    val data: DailyCalendarData
)

data class DailyCalendarData(
    val userId: Long,
    val year: Int,
    val month: Int,
    val records: List<DailyCalendarRecord>
)

data class DailyCalendarRecord(
    val recordId: Long,
    val category: String,
    val occuredAt: String,
    val updatedAt: String,
    val grade: String,
    val dbHigh: Double,
    val dbAvg: Double,
    val description: String?,  // 사용자가 작성한 메모 (소음일기용)
    val summary: String?       // AI가 자동 생성한 요약 (소음현황판용)
) {
    /**
     * Response String 날짜를 Domain 모델로 변환
     * 소음일기에서는 description 우선 표시
     */
    fun toDomain(): NoiseLog {
        val measuredDate = parseStringToDate(occuredAt)

        // 소음일기는 사용자가 작성한 description을 표시
        val memoText = description ?: ""

        return NoiseLog(
            id = recordId.toString(),
            noiseType = category,
            maxDecibel = dbHigh,
            avgDecibel = dbAvg,
            memo = memoText,
            measuredAt = measuredDate,
            hasReport = false
        )
    }

    private fun parseStringToDate(dateString: String): Date {
        val localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return Date.from(localDateTime.atZone(ZoneOffset.systemDefault()).toInstant())
    }
}
