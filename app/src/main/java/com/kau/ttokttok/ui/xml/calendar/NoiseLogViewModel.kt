package com.kau.ttokttok.ui.xml.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * 소음 일기 기능 ViewModel
 * - 전체 일기 목록 관리
 * - 날짜별 일기 필터링
 * - 일기 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 리포트 생성 상태 관리
 */
@HiltViewModel
class NoiseLogViewModel @Inject constructor(
    private val repository: NoiseLogRepository // Hilt가 자동 주입
) : ViewModel() {

    // 전체 소음 일기 목록
    private val _noiseLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val noiseLogs: StateFlow<List<NoiseLog>> = _noiseLogs.asStateFlow()

    // 선택된 날짜
    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate.asStateFlow()

    // 선택된 날짜의 일기 목록
    private val _selectedLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val selectedLogs: StateFlow<List<NoiseLog>> = _selectedLogs.asStateFlow()

    init {
        loadAllLogs() // ViewModel 생성 시 전체 일기 로드
    }

    // TODO: [백엔드 연동] 로딩/에러 상태 관리를 위한 StateFlow 추가
    // private val _isLoading = MutableStateFlow(false)
    // val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // private val _errorState = MutableStateFlow<String?>(null)
    // val errorState: StateFlow<String?> = _errorState.asStateFlow()

    // 모든 일기 로드 - Repository에서 서버 또는 로컬 DB 데이터 가져옴
    fun loadAllLogs() {
        viewModelScope.launch {
            // TODO: [백엔드 연동] _isLoading.value = true
            repository.getAllNoiseLogs().onSuccess { logs ->
                _noiseLogs.value = logs
                // TODO: [백엔드 연동] _errorState.value = null
            }.onFailure { error ->
                // TODO: [백엔드 연동] _errorState.value = error.message
                // TODO: [백엔드 연동] 네트워크 오류 시 로컬 캐시 데이터 사용 고려
            }
            // TODO: [백엔드 연동] _isLoading.value = false
        }
    }

    // 날짜 선택 및 해당 날짜의 일기 로드 - Repository를 통해 필터링된 데이터 가져옴
    fun selectDate(date: Date) {
        _selectedDate.value = date
        viewModelScope.launch {
            // TODO: [백엔드 연동] 서버에 날짜별 조회 API 호출 (GET /api/noise-logs?date=yyyy-MM-dd)
            // TODO: [백엔드 연동] 또는 로컬에서 필터링 (현재 구현 방식)
            repository.getNoiseLogsByDate(date).onSuccess { logs ->
                _selectedLogs.value = logs
            }.onFailure { error ->
                // TODO: [백엔드 연동] _errorState.value = "날짜별 로그 조회 실패: ${error.message}"
            }
        }
    }

    // 일기 삭제 - Repository를 통해 서버와 로컬 DB에서 삭제
    fun deleteLog(id: String) {
        viewModelScope.launch {
            // TODO: [백엔드 연동] 낙관적 업데이트: UI에서 먼저 삭제 표시 후 서버 요청
            repository.deleteNoiseLog(id).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 삭제 실패 시 롤백 및 사용자에게 알림
                // TODO: [백엔드 연동] _errorState.value = "삭제 실패: ${error.message}"
            }
        }
    }

    // 리포트 상태 토글 - 실제로는 별도의 리포트 생성 API 호출 필요
    fun toggleReportStatus(log: NoiseLog) {
        viewModelScope.launch {
            // TODO: [백엔드 연동] 리포트 생성은 별도 API 사용 (POST /api/reports)
            // TODO: [백엔드 연동] 요청: { noiseLogIds: [log.id], reportType: "monthly" }
            // TODO: [백엔드 연동] 응답: { reportId, pdfUrl, createdAt }
            val updated = log.copy(hasReport = !log.hasReport)
            repository.updateNoiseLog(updated).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] _errorState.value = "리포트 생성 실패: ${error.message}"
            }
        }
    }

    // 새로운 소음 일기 저장 - Repository를 통해 서버에 저장
    fun saveLog(log: NoiseLog) {
        viewModelScope.launch {
            // TODO: [백엔드 연동] POST /api/noise-logs
            // TODO: [백엔드 연동] 요청 본문: NoiseLog 객체 (id는 null)
            // TODO: [백엔드 연동] 응답: 서버에서 생성한 ID 포함된 NoiseLog 객체
            repository.saveNoiseLog(log).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 네트워크 오류 시 로컬에 임시 저장 (offline-first 전략)
                // TODO: [백엔드 연동] _errorState.value = "저장 실패: ${error.message}"
            }
        }
    }

    // 소음 일기 수정 - Repository를 통해 서버의 데이터 업데이트
    fun updateLog(log: NoiseLog) {
        viewModelScope.launch {
            // TODO: [백엔드 연동] PUT /api/noise-logs/{id}
            // TODO: [백엔드 연동] 수정 권한 확인 (본인이 작성한 일기인지 서버에서 검증)
            repository.updateNoiseLog(log).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                // TODO: [백엔드 연동] 권한 없음(403) 또는 수정 실패 에러 처리
                // TODO: [백엔드 연동] _errorState.value = "수정 실패: ${error.message}"
            }
        }
    }
}
