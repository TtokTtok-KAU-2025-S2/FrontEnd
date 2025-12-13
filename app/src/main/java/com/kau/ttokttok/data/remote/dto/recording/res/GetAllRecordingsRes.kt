package com.kau.ttokttok.data.remote.dto.recording.res

/**
 * 녹음 DB 모두 불러오기 API 응답 DTO
 * GET /api/recordings
 */
data class RecordingItem(
    val recordingId: Long,
    val fileUrl: String,
    val originalFileName: String,
    val duration: Int?,
    val dbMax: Double?,
    val dbAvg: Double?,
    val createdAt: String
)

