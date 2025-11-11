package com.kau.ttokttok.data.remote.dto.noisecalendar.res

data class GetMonthlyNoiseCalendarRes(
    val month: Int,
    val data: List<Day>,
    val year: Int
)

data class Day(
    val date: String,
    val hasNoiseLog: Boolean
)
