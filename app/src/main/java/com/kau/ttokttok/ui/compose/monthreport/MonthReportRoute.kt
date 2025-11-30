package com.kau.ttokttok.ui.compose.monthreport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MonthReportRoute(
    viewModel: MonthReportViewModel = hiltViewModel(),
    onClickMoreInformation: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MonthReportScreen(
        uiState = uiState,
        onClickMoreInformation = onClickMoreInformation
    )
}