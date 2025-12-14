package com.kau.ttokttok.data.remote.dto.recording.res

import com.squareup.moshi.Json

data class UploadRecordingRes(
    @Json(name = "recordingId")
    val recordingId: Long,

    @Json(name = "fileUrl")
    val fileUrl: String,

    @Json(name = "createdAt")
    val createdAt: String
)

