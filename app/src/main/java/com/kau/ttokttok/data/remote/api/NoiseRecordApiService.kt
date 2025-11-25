package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.noiserecord.req.CreateNoiseRecordReq
import com.kau.ttokttok.data.remote.dto.noiserecord.req.ModifyNoiseRecordReq
import com.kau.ttokttok.data.remote.dto.noiserecord.res.CreateNoiseRecordRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.DeleteNoiseRecordRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.GetAllNoiseRecordCountRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.GetAverageNoiseDBRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.GetMonthlyNoiseRecordCountRes
import com.kau.ttokttok.data.remote.dto.noiserecord.res.ModifyNoiseRecordRes
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NoiseRecordApiService {
    @GET("noise/records/total-count")
    suspend fun getAllNoiseRecordCount(): ApiResponse<GetAllNoiseRecordCountRes>

    @GET("noise/records/monthly-count")
    suspend fun getMonthlyNoiseRecordCount(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<GetMonthlyNoiseRecordCountRes>

    @GET("noise/records/average-db")
    suspend fun getAverageNoiseDB(): ApiResponse<GetAverageNoiseDBRes>

    // 생성
    @POST("noise/records")
    suspend fun createNoiseRecord(
        @Body req: CreateNoiseRecordReq
    ): ApiResponse<CreateNoiseRecordRes>

    // 수정
    @PATCH("noise/records/{recordId}")
    suspend fun modifyNoiseRecord(
        @Path("recordId") id: Long,
        @Body req: ModifyNoiseRecordReq
    ): ApiResponse<ModifyNoiseRecordRes>

    // 삭제
    @DELETE("noise/records/{recordId}")
    suspend fun deleteNoiseRecord(
        @Path("recordId") id: Long
    ): ApiResponse<DeleteNoiseRecordRes>
}