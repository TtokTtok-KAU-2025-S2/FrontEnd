package com.kau.ttokttok.domain.usecase.monthreport

data class GetMonthReportResult(
    val totalReportCount: Int,
    val changeRate: Double,
    val aiAnalysisText: String,

    val hourlyStats: Map<String, Int>,
    val categoryStats: Map<String, Int>
)
