package com.kau.ttokttok.data.remote.dto.report.res

import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportResult

data class GetApartMonthReportRes(
    val totalReportCount: Int,
    val changeRate: Double,
    val aiAnalysisText: String,

    val hourlyStats: Map<String, Int>,
    val categoryStats: Map<String, Int>,
) {
    fun toResult(): GetMonthReportResult {
        return GetMonthReportResult(
            totalReportCount = totalReportCount,
            changeRate = changeRate,
            aiAnalysisText = aiAnalysisText,

            hourlyStats = hourlyStats,
            categoryStats = categoryStats
        )
    }
}