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
    val summary: String?,
    val description: String?  // 등록 시 서버가 description으로 보낼 수 있으므로 추가
) {
    /**
     * Response String 날짜를 Domain 모델로 변환
     */
    fun toDomain(): NoiseLog {
        val measuredDate = parseStringToDate(occuredAt)

        // 메모 우선순위: summary(수정 시) > description(등록 시) > 빈 문자열
        val memoText = when {
            !summary.isNullOrBlank() -> summary
            !description.isNullOrBlank() -> description
            else -> ""
        }

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
