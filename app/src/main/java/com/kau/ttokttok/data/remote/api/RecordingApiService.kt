package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.recording.res.UploadRecordingRes
import okhttp3.MultipartBody
import retrofit2.http.*

interface RecordingApiService {
    @Multipart
    @POST("api/recordings")
    suspend fun uploadRecording(
        @Part voiceFile: MultipartBody.Part
    ): ApiResponse<UploadRecordingRes>
}

