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
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * 소음 일기 기능 ViewModel
 * - 날짜별 소음 기록 조회 (API)
 * - 월별 캘린더 데이터 조회 (API)
 * - 소음 통계 조회: 총 개수, 월별 개수, 평균 dB (API)
 * - 일기 CRUD 작업 (생성, 수정, 삭제)
 * - 리포트 생성 상태 관리
 */
@HiltViewModel
class NoiseLogViewModel @Inject constructor(
    private val repository: NoiseLogRepository // Hilt가 자동 주입
) : ViewModel() {

    // 선택된 날짜
    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate.asStateFlow()

    // 선택된 날짜의 일기 목록
    private val _selectedLogs = MutableStateFlow<List<NoiseLog>>(emptyList())
    val selectedLogs: StateFlow<List<NoiseLog>> = _selectedLogs.asStateFlow()

    // 캘린더 데이터 (날짜 -> 소음 일기 존재 여부)
    private val _calendarData = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val calendarData: StateFlow<Map<String, Boolean>> = _calendarData.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 상태
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    // 총 소음 기록 수
    private val _totalCount = MutableStateFlow(0)
    val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    // 이번 달 소음 기록 수
    private val _monthlyCount = MutableStateFlow(0)
    val monthlyCount: StateFlow<Int> = _monthlyCount.asStateFlow()

    // 전체 소음 기록 평균 dB
    private val _averageDb = MutableStateFlow(0.0)
    val averageDb: StateFlow<Double> = _averageDb.asStateFlow()

    // 날짜 선택 및 해당 날짜의 일기 로드 - API를 통해 서버에서 데이터 가져옴
    fun selectDate(date: Date) {
        _selectedDate.value = date
        // Date를 yyyy-MM-dd 형식으로 변환하여 API 호출
        val dateString = formatDate(date)
        loadNoiseRecordsByDate(dateString)
    }

    // 일기 삭제 - Repository를 통해 서버와 로컬 DB에서 삭제
    fun deleteLog(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteNoiseLog(id).onSuccess {
                _errorState.value = null
                // 삭제 후 현재 날짜의 데이터만 새로고침
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                _errorState.value = "삭제 실패: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    // 리포트 상태 토글 - 실제로는 별도의 리포트 생성 API 호출 필요
    fun toggleReportStatus(log: NoiseLog) {
        viewModelScope.launch {
            _isLoading.value = true
            val updated = log.copy(hasReport = !log.hasReport)
            repository.updateNoiseLog(updated).onSuccess {
                _errorState.value = null
                // 리포트 상태 변경 후 현재 날짜의 데이터만 새로고침
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                _errorState.value = "리포트 생성 실패: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    // 새로운 소음 일기 저장 - Repository를 통해 서버에 저장
    fun saveLog(log: NoiseLog) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.saveNoiseLog(log).onSuccess {
                _errorState.value = null
                // 저장 후 해당 날짜의 데이터만 새로고침
                selectDate(_selectedDate.value)
            }.onFailure { error ->
                _errorState.value = "저장 실패: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    /**
     * 캘린더 월별 데이터 로드 - 백엔드 API 호출
     * @param userId 회원 고유 ID
     * @param year 조회할 연도
     * @param month 조회할 월 (1~12)
     */
    fun loadCalendarData(userId: Long, year: Int, month: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.getCalendarData(userId, year, month)
                .onSuccess { calendarMap ->
                    _calendarData.value = calendarMap
                    _errorState.value = null
                }
                .onFailure { error ->
                    _errorState.value = "캘린더 데이터 로드 실패: ${error.message}"
                    _calendarData.value = emptyMap()
                }

            _isLoading.value = false
        }
    }

    /**
     * 현재 월의 캘린더 데이터 로드
     */
    fun loadCurrentMonthCalendar(userId: Long) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작
        loadCalendarData(userId, year, month)
    }

    /**
     * 에러 상태 초기화
     */
    fun clearError() {
        _errorState.value = null
    }

    /**
     * 날짜별 소음 기록 상세 조회 - 백엔드 API 호출
     * @param date 조회할 날짜 (yyyy-MM-dd 형식)
     */
    fun loadNoiseRecordsByDate(date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.getNoiseRecordsByDate(date)
                .onSuccess { logs ->
                    _selectedLogs.value = logs
                    _errorState.value = null
                }
                .onFailure { error ->
                    _errorState.value = "소음 기록 조회 실패: ${error.message}"
                    _selectedLogs.value = emptyList()
                }

            _isLoading.value = false
        }
    }

    /**
     * Date 객체를 yyyy-MM-dd 형식의 문자열로 변환
     */
    private fun formatDate(date: Date): String {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(date)
    }

    /**
     * 총 소음 기록 수 조회 - 백엔드 API 호출
     * @param userId 조회할 사용자의 ID
     */
    fun loadTotalCount(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.getTotalCount(userId)
                .onSuccess { count ->
                    _totalCount.value = count
                    _errorState.value = null
                }
                .onFailure { error ->
                    _errorState.value = "총 기록 수 조회 실패: ${error.message}"
                    _totalCount.value = 0
                }

            _isLoading.value = false
        }
    }

    /**
     * 이번 달 소음 기록 수 조회 - 백엔드 API 호출
     * @param userId 조회할 사용자의 ID
     */
    fun loadMonthlyCount(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작

            repository.getMonthlyCount(userId, year, month)
                .onSuccess { count ->
                    _monthlyCount.value = count
                    _errorState.value = null
                }
                .onFailure { error ->
                    _errorState.value = "이번 달 기록 수 조회 실패: ${error.message}"
                    _monthlyCount.value = 0
                }

            _isLoading.value = false
        }
    }

    /**
     * 전체 소음 기록 평균 dB 조회 - 백엔드 API 호출
     * @param userId 조회할 사용자의 ID
     */
    fun loadAverageDb(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.getAverageDb(userId)
                .onSuccess { avgDb ->
                    _averageDb.value = avgDb
                    _errorState.value = null
                }
                .onFailure { error ->
                    _errorState.value = "평균 dB 조회 실패: ${error.message}"
                    _averageDb.value = 0.0
                }

            _isLoading.value = false
        }
    }

    /**
     * 소음 일기 등록 (API 연동) - 백엔드 API 호출
     * @param occuredAt 소음 발생 시간 (ISO 8601 형식)
     * @param duration 측정 소요 시간(녹음 duration)
     * @param dbHigh 최대 데시벨
     * @param dbAvg 평균 데시벨
     * @param category 소음 카테고리 (FOOTSTEPS, HAMMERING, FURNITURE, MUSIC, UNKNOWN)
     * @param grade 소음 등급 (QUIET, NORMAL, LOUD)
     * @param description 소음일기 내용
     */
    fun createNoiseRecordApi(
        occuredAt: String,
        duration: Int,
        dbHigh: Double,
        dbAvg: Double,
        category: String,
        grade: String,
        description: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.createNoiseRecordApi(
                occuredAt = occuredAt,
                duration = duration,
                dbHigh = dbHigh,
                dbAvg = dbAvg,
                category = category,
                grade = grade,
                description = description
            )
                .onSuccess { createdLog ->
                    _errorState.value = null
                    // 등록 성공 시 현재 날짜의 데이터만 새로고침
                    selectDate(_selectedDate.value)
                }
                .onFailure { error ->
                    _errorState.value = "소음 일기 등록 실패: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    /**
     * 소음 일기 수정 (API 연동) - 백엔드 API 호출
     * @param recordId 수정할 소음 기록의 고유 ID
     * @param log 수정할 소음 일기 데이터
     */
    fun updateLogApi(recordId: Long, log: NoiseLog) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.updateNoiseRecordApi(recordId, log)
                .onSuccess { updatedLog ->
                    _errorState.value = null
                    // 수정 성공 시 현재 날짜의 데이터만 새로고침
                    selectDate(_selectedDate.value)
                }
                .onFailure { error ->
                    _errorState.value = "소음 일기 수정 실패: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    /**
     * 소음 일기 삭제 (API 연동 - Hard Delete) - 백엔드 API 호출
     * @param recordId 삭제할 소음 기록의 고유 ID
     */
    fun deleteNoiseRecordApi(recordId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            repository.deleteNoiseRecordApi(recordId)
                .onSuccess { deletedRecordId ->
                    _errorState.value = null
                    // 삭제 성공 시 현재 날짜의 데이터만 새로고침
                    selectDate(_selectedDate.value)
                }
                .onFailure { error ->
                    _errorState.value = "소음 일기 삭제 실패: ${error.message}"
                }

            _isLoading.value = false
        }
    }
}
