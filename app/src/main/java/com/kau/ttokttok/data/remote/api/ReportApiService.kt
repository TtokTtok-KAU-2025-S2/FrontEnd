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

    // 리포트 생성(전송)
    @POST("noise/records/{recordId}/send")
    suspend fun createReport(
        @Path("recordId") recordId: Long
    ): ApiResponse<CreateReportRes>
}