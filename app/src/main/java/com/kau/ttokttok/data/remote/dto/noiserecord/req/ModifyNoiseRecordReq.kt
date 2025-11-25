package com.kau.ttokttok.data.remote.dto.noiserecord.req

data class ModifyNoiseRecordReq(
    val category: String,
    val occuredAt: String,
    val noiseGrade: String,
    val dbHigh: Double,
    val dbAvg: Double,
    val summary: String?
)

