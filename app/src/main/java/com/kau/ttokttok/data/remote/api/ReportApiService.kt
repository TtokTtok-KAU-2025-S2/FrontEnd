package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.report.res.GetApartmentStatsRes
import com.kau.ttokttok.data.remote.dto.report.res.GetMonthlyReportRes
import retrofit2.http.GET
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
}