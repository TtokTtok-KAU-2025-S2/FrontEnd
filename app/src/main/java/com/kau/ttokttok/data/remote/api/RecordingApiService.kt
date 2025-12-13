package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.recording.res.RecordingItem
import com.kau.ttokttok.data.remote.dto.recording.res.UploadRecordingRes
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RecordingApiService {
    @Multipart
    @POST("api/recordings")
    suspend fun uploadRecording(
        @Part voiceFile: MultipartBody.Part
    ): ApiResponse<UploadRecordingRes>

    @GET("api/recordings")
    suspend fun getAllRecordings(): ApiResponse<List<RecordingItem>>
}

