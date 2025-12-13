package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.data.remote.dto.recording.res.RecordingItem
import com.kau.ttokttok.data.remote.dto.recording.res.UploadRecordingRes
import java.io.File

interface RecordingRepository {
    suspend fun uploadRecording(file: File): Result<UploadRecordingRes>
    suspend fun getAllRecordings(): Result<List<RecordingItem>>
}

