package com.kau.ttokttok.data.remote.dto.noiserecord.res

import java.time.LocalDateTime

data class ModifyNoiseRecordRes(
    val noiseId: Long,
    // TODO: 추후 Enum으로 변경
    val category: String,
    val occuredAt: LocalDateTime,
    // TODO: 추후 Enum으로 변경
    val noiseGrade: String,
    val dbHigh: Int,
    val dbAvg: Int,
    val summary: String,
    val updatedAt: LocalDateTime
)
