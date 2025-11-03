package com.kau.ttokttok.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.data.local.repository.NoiseLogRepositoryImpl
import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class NoiseLogViewModel(
    private val repository: NoiseLogRepository = NoiseLogRepositoryImpl()
) : ViewModel() {

    private val _noiseLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val noiseLogs: StateFlow<List<NoiseLog>> = _noiseLogs.asStateFlow()

    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate.asStateFlow()

    private val _selectedLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val selectedLogs: StateFlow<List<NoiseLog>> = _selectedLogs.asStateFlow()

    init {
        loadAllLogs()
    }

    // 모든 일기 로드
    fun loadAllLogs() {
        viewModelScope.launch {
            repository.getAllNoiseLogs().onSuccess { logs ->
                _noiseLogs.value = logs
            }
        }
    }

    // 날짜 선택 및 해당 날짜의 일기 로드
    fun selectDate(date: Date) {
        _selectedDate.value = date
        viewModelScope.launch {
            repository.getNoiseLogsByDate(date).onSuccess { logs ->
                _selectedLogs.value = logs
            }
        }
    }

    // 일기 삭제
    fun deleteLog(id: String) {
        viewModelScope.launch {
            repository.deleteNoiseLog(id).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }
        }
    }

    // 리포트 상태 토글
    fun toggleReportStatus(log: NoiseLog) {
        viewModelScope.launch {
            val updated = log.copy(hasReport = !log.hasReport)
            repository.updateNoiseLog(updated).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }
        }
    }

    // 새로운 소음 일기 저장
    fun saveLog(log: NoiseLog) {
        viewModelScope.launch {
            repository.saveNoiseLog(log).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }
        }
    }
}
