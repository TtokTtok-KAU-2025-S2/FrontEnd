package com.kau.ttokttok.data.repository

import com.kau.ttokttok.data.remote.api.RecordingApiService
import com.kau.ttokttok.data.remote.dto.recording.res.UploadRecordingRes
import com.kau.ttokttok.domain.repository.RecordingRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class RecordingRepositoryImpl @Inject constructor(
    private val recordingApiService: RecordingApiService
) : RecordingRepository {

    override suspend fun uploadRecording(file: File): Result<UploadRecordingRes> {
        return try {
            // AAC/M4A 오디오 파일로 전송
            val requestFile = file.asRequestBody("audio/aac".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("voiceFile", file.name, requestFile)

            val response = recordingApiService.uploadRecording(multipartBody)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

