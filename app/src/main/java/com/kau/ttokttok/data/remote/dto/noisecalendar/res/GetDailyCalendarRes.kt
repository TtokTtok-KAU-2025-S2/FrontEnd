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
    val dbHigh: Int,
    val dbAvg: Int,
    val summary: String?
)
