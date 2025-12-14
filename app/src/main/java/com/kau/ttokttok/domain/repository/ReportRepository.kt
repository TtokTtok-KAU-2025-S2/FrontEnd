package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.report.res.GetApartmentStatsRes
import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportResult

interface ReportRepository {
    suspend fun getApartmentStats(keyword: String): NetworkResult<GetApartmentStatsRes>
    suspend fun getMonthlyReport(date: String): Result<GetMonthReportResult>
}