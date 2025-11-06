package com.kau.ttokttok.ui.xml.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    // TODO: [백엔드 연동] 에러 상태 관리 추가 (네트워크 오류, 로딩 상태 등)
    fun loadAllLogs() {
        viewModelScope.launch {
            // TODO: [백엔드 연동] 로딩 상태 표시 (_isLoading.value = true)
            repository.getAllNoiseLogs().onSuccess { logs ->
                _noiseLogs.value = logs
            }.onFailure { error ->
                // TODO: [백엔드 연동] 에러 처리 (토스트 메시지, 재시도 옵션 등)
                // _error.value = error.message
            }
            // TODO: [백엔드 연동] 로딩 완료 (_isLoading.value = false)
        }
    }

    // 날짜 선택 및 해당 날짜의 일기 로드
    // TODO: [백엔드 연동] 캐싱 전략 고려 (같은 날짜를 여러 번 조회할 때)
    fun selectDate(date: Date) {
        _selectedDate.value = date
        viewModelScope.launch {
            repository.getNoiseLogsByDate(date).onSuccess { logs ->
                _selectedLogs.value = logs
            }.onFailure { error ->
                // TODO: [백엔드 연동] 에러 처리
            }
        }
    }

    // 일기 삭제
    // TODO: [백엔드 연동] 삭제 확인 후 낙관적 업데이트 고려
    fun deleteLog(id: String) {
        viewModelScope.launch {
            repository.deleteNoiseLog(id).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 삭제 실패 시 사용자에게 알림
            }
        }
    }

    // 리포트 상태 토글
    // TODO: [백엔드 연동] 실제로는 별도의 리포트 생성 API를 호출해야 할 수 있음
    // TODO: [백엔드 연동] POST /api/reports { noiseLogIds: [...] }
    fun toggleReportStatus(log: NoiseLog) {
        viewModelScope.launch {
            val updated = log.copy(hasReport = !log.hasReport)
            repository.updateNoiseLog(updated).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 리포트 생성 실패 시 에러 처리
            }
        }
    }

    // 새로운 소음 일기 저장
    // TODO: [백엔드 연동] 저장 성공 시 서버에서 받은 ID로 업데이트
    fun saveLog(log: NoiseLog) {
        viewModelScope.launch {
            repository.saveNoiseLog(log).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 저장 실패 시 에러 처리 (네트워크 오류, 검증 오류 등)
            }
        }
    }

    // 소음 일기 수정
    // TODO: [백엔드 연동] 수정 권한 확인 (본인이 작성한 일기인지)
    fun updateLog(log: NoiseLog) {
        viewModelScope.launch {
            repository.updateNoiseLog(log).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 수정 실패 시 에러 처리
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: NoiseLogRepository = NoiseLogRepositoryImpl()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoiseLogViewModel(repository) as T
            }
        }
    }
}
