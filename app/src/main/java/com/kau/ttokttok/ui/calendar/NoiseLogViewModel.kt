package com.kau.ttokttok.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoiseLogViewModel @Inject constructor(
    private val repository: NoiseLogRepository
) : ViewModel() {

    private val _noiseLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val noiseLogs: StateFlow<List<NoiseLog>> = _noiseLogs

    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate

    private val _selectedLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val selectedLogs: StateFlow<List<NoiseLog>> = _selectedLogs

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
}
