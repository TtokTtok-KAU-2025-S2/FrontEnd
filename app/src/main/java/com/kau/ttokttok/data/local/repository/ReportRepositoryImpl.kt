package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.ReportApiService
import com.kau.ttokttok.data.remote.dto.report.res.GetApartmentStatsRes
import com.kau.ttokttok.data.remote.dto.report.res.GetMonthlyReportRes
import com.kau.ttokttok.domain.repository.ReportRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApiService
): ReportRepository {
    override suspend fun getApartmentStats(keyword: String): NetworkResult<GetApartmentStatsRes> =
        safeApiCall { api.getApartmentStats(keyword) }

    override suspend fun getMonthlyReport(date: String): NetworkResult<GetMonthlyReportRes> =
        safeApiCall { api.getMonthlyReport(date)}
}