package com.kau.ttokttok.data.remote.dto.noisecalendar.res

data class GetMonthlyNoiseCalendarRes(
    val userId: Long,
    val year: Int,
    val month: Int,
    val data: MonthlyCalendarData
)

data class MonthlyCalendarData(
    val userId: Long,
    val year: Int,
    val month: Int,
    val dates: List<MonthlyCalendarDate>
)

data class MonthlyCalendarDate(
    val date: String,
    val hasNoiseLog: Boolean
)
