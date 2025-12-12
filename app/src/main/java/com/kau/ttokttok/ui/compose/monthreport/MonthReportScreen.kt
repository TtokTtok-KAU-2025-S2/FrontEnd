package com.kau.ttokttok.ui.compose.monthreport

import NationalDataPreviewCard
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.monthreport.*
import com.kau.ttokttok.ui.theme.*
import java.time.LocalDate

@Preview(heightDp = 1300)
@Composable
fun MonthReportScreen(
    modifier: Modifier = Modifier,
    uiState: MonthReportUiState = MonthReportUiState(),
    onClickMoreInformation: () -> Unit = {}
) {
    val now = LocalDate.now().minusMonths(1)

    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Slate900)
        ) {
            ReportHeader(
                modifier = modifier,
                dateText = String.format("%s년 %s월", now.year, now.monthValue)
            )

            Spacer(modifier.height(32.dp))

            NoiseSummaryCard(
                totalReports = uiState.totalReports,
                comparedToPrevious = uiState.comparedToPrevious
            )

            Spacer(modifier.height(32.dp))

            AiSummaryCard(
                summaryText = uiState.summaryText
            )

            Spacer(modifier.height(32.dp))

            NoisePeakHoursCard(
                hourlyStats = uiState.hourlyStats
            )

            Spacer(modifier.height(32.dp))

            NoiseTypesCard(
                categoryStats = uiState.categoryStats
            )

            Spacer(modifier.height(32.dp))

            NationalDataPreviewCard(
                onViewNationalData = onClickMoreInformation
            )
        }
    }
}