package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.report.res.CreateReportRes
import com.kau.ttokttok.data.remote.dto.report.res.GetApartmentStatsRes
import com.kau.ttokttok.data.remote.dto.report.res.GetMonthlyReportRes
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportApiService {
    @GET("api/stats/nationwide")
    suspend fun getApartmentStats(
        @Query("keyword") keyword: String
    ): ApiResponse<GetApartmentStatsRes>

    @GET("api/reports/monthly")
    suspend fun getMonthlyReport(
        @Query("date") date: String
    ): ApiResponse<GetMonthlyReportRes>

    /**
     * 소음 일기를 소음현황판으로 전송
     * - 서버에서 AI가 자동으로 summary(요약) 생성
     * - description(사용자 메모)을 기반으로 AI 요약 생성
     * - 생성된 게시글의 ID 반환
     */
    @POST("noise/records/{recordId}/send")
    suspend fun createReport(
        @Path("recordId") recordId: Long
    ): ApiResponse<CreateReportRes>
}