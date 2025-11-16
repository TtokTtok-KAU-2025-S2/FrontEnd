package com.kau.ttokttok.data.remote.dto.noiserecord.res

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
    // 서버에서 LocalDateTime 문자열을 내려주므로, Moshi 커스텀 어댑터 없이 String으로 수신
    val occuredAt: String,
    val updateAt: String
)
