package com.kau.ttokttok.data.remote.dto.noiserecord.res

import java.time.LocalDateTime

data class CreateNoiseRecordRes(
    val id: Long,
    val userId: Long,
    val duration: Int,
    val dbHigh: Int,
    val dbAvg: Int,
    val category: String,
    val grade: String,
    val description: String?,
    val summary: String?,
    val occuredAt: LocalDateTime,
    val updateAt: LocalDateTime
)

