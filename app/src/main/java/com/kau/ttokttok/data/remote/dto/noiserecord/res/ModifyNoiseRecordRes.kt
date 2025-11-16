package com.kau.ttokttok.data.remote.dto.noiserecord.res

data class ModifyNoiseRecordRes(
    val noiseId: Long,
    // TODO: 추후 Enum으로 변경
    val category: String,
    // 서버에서 LocalDateTime 문자열을 내려주므로 문자열로 수신
    val occuredAt: String,
    // TODO: 추후 Enum으로 변경
    val noiseGrade: String,
    val dbHigh: Int,
    val dbAvg: Int,
    val summary: String,
    val updatedAt: String
)
