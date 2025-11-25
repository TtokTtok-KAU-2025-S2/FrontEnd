package com.kau.ttokttok.data.remote.dto.noisecalendar.res

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
)
