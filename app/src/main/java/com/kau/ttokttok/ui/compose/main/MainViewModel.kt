package com.kau.ttokttok.ui.compose.main

import androidx.lifecycle.ViewModel
import com.kau.ttokttok.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {
    private val _buildingNumber = MutableStateFlow<Int?>(null)
    val buildingNumber: StateFlow<Int?> = _buildingNumber

    private val _unitNumber = MutableStateFlow<Int?>(null)
    val unitNumber: StateFlow<Int?> = _unitNumber

    fun getUserInformation() {
        val buildingNumber = mainUseCase.getBuildingNumber()
        val unitNumber = mainUseCase.getUnitNumber()

        _buildingNumber.value = buildingNumber
        _unitNumber.value = unitNumber
    }
}