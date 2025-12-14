package com.kau.ttokttok.data.remote.repository

import com.kau.ttokttok._core.network.result.*
import com.kau.ttokttok.data.remote.api.ReportApiService
import com.kau.ttokttok.data.remote.dto.report.res.GetApartmentStatsRes
import com.kau.ttokttok.domain.repository.ReportRepository
import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApiService
): ReportRepository {
    override suspend fun getApartmentStats(keyword: String): NetworkResult<GetApartmentStatsRes> =
        safeApiCall { api.getApartmentStats(keyword) }

    override suspend fun getMonthlyReport(date: String): Result<GetMonthReportResult> {
        return when (val result = safeApiCall { api.getMonthlyReport(date) }) {
            is NetworkResult.Success -> {
                Result.success(result.data.toResult())
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(result.message ?: "Network Error"))
            }
        }
    }
}