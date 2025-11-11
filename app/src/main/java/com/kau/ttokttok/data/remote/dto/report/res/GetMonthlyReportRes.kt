package com.kau.ttokttok.data.remote.dto.report.res

data class GetMonthlyReportRes(
    val apartmentName: String,
    val year: Int,
    val month: Int,
    val totalReportCount: Int,
    val changeRate: Double,
    val aiAnalysisText: String,

    // TODO: 추후 Enum으로 변경
    val hourlystats: List<Int>,
    // val categoryStats: List<Category>,
)
