package com.kau.ttokttok.data.remote.dto.report.res

data class GetApartMonthReportRes(
    val totalReportCount: Int,
    val changeRate: Double,
    val aiAnalysisText: String,

    val hourlyStats: Map<String, Int>,
    val categoryStats: Map<String, Int>,
)