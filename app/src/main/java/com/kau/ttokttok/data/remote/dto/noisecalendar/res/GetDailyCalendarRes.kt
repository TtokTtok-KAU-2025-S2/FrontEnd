package com.kau.ttokttok.data.remote.dto.noisecalendar.res

data class GetDailyCalendarRes(
    val recordId: Long,
    val logDate: String,
    val logTime: String,
    // TODO: 추후 Enum으로 바꾸기
    val noiseType: String,
    val noiseLevel: Int,
    val memo: String,
    val createdAt: String,
    val modifiedAt: String
)
