package com.kau.ttokttok.data.remote.dto.noiserecord.req

data class CreateNoiseRecordReq(
    val occuredAt: String,   // ISO-8601 (e.g., 2025-11-09T11:32:57.272Z)
    val duration: Int,
    val dbHigh: Double,
    val dbAvg: Double,
    val category: String,    // FOOTSTEPS, HAMMERING, FURNITURE, MUSIC, UNKNOWN
    val grade: String,       // QUIET, NORMAL, LOUD
    val description: String,
    // 서버가 텍스트 메모를 summary로 저장할 수 있도록 등록 시에도 함께 전달
    val summary: String?
)
