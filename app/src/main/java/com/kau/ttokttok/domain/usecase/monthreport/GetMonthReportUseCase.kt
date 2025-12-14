package com.kau.ttokttok.domain.usecase.monthreport

import com.kau.ttokttok.domain.repository.ReportRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMonthReportUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(): Result<GetMonthReportResult> {
        val now = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE)

        return repository.getMonthlyReport(now)
    }
}