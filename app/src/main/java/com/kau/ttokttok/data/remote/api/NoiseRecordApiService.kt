package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.noiserecord.res.GetAllNoiseRecordCountRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.GetAverageNoiseDBRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.GetMonthlyNoiseRecordCountRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.ModifyNoiseRecordRes
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NoiseRecordApiService {
    @GET("api/noise/records/total-count")
    suspend fun getAllNoiseRecordCount(): ApiResponse<GetAllNoiseRecordCountRes>

    @GET("api/noise/records/monthly-count")
    suspend fun getMonthlyNoiseRecordCount(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<GetMonthlyNoiseRecordCountRes>

    @GET("api/noise/records/average-db")
    suspend fun getAverageNoiseDB(): ApiResponse<GetAverageNoiseDBRes>

    @PATCH("api/noise/records/{recordId}")
    suspend fun modifyNoiseRecord(
        @Path("recordId") id: Long,
    ): ApiResponse<ModifyNoiseRecordRes>

    // TODO: 아래 메소드들은 BE 준비중
}