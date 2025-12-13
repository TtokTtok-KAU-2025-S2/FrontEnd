package com.kau.ttokttok.data.repository

import com.kau.ttokttok.data.remote.api.RecordingApiService
import com.kau.ttokttok.data.remote.dto.recording.res.RecordingItem
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

            val apiResponse = recordingApiService.uploadRecording(multipartBody)

            // ApiResponse 래퍼를 벗겨서 처리
            if (apiResponse.isSuccess && apiResponse.result != null) {
                Result.success(apiResponse.result)
            } else {
                Result.failure(Exception("업로드 실패: ${apiResponse.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllRecordings(): Result<List<RecordingItem>> {
        return try {
            val apiResponse = recordingApiService.getAllRecordings()

            if (apiResponse.isSuccess && apiResponse.result != null) {
                Result.success(apiResponse.result)
            } else {
                Result.failure(Exception("녹음 목록 조회 실패: ${apiResponse.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

