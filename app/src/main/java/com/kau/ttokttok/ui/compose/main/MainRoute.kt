package com.kau.ttokttok.ui.compose.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.navigation.Destination

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigate: (Destination) -> Unit
) {
    val buildingNumber by viewModel.buildingNumber.collectAsStateWithLifecycle()
    val unitNumber by viewModel.unitNumber.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.getUserInformation()
    }

    MainScreen(
        onNavigate = onNavigate,
        buildingNumber = buildingNumber ?: 0,
        unitNumber = unitNumber ?: 0
    )
}