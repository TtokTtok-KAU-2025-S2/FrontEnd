package com.kau.ttokttok.ui.xml.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.NoiseCalendarApiService
import com.kau.ttokttok.data.remote.api.NoiseRecordApiService
import com.kau.ttokttok.data.remote.api.ReportApiService
import com.kau.ttokttok.data.remote.dto.noiserecord.req.CreateNoiseRecordReq
import com.kau.ttokttok.data.remote.dto.noiserecord.req.ModifyNoiseRecordReq
import com.kau.ttokttok.data.remote.dto.report.req.CreateReportReq
import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject
import com.kau.ttokttok.data.remote.dto.noisecalendar.res.GetDailyCalendarRes
import com.kau.ttokttok.data.remote.dto.noisecalendar.res.DailyCalendarRecord
import java.time.LocalDateTime

/**
 * 소음 일기 기능 ViewModel
 * - 전체 일기 목록 관리
 * - 날짜별 일기 필터링
 * - 일기 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 리포트 생성 상태 관리
 */
@HiltViewModel
class NoiseLogViewModel @Inject constructor(
    private val repository: NoiseLogRepository, // Hilt가 자동 주입
    private val noiseRecordApiService: NoiseRecordApiService,
    private val noiseCalendarApiService: NoiseCalendarApiService,
    private val reportApiService: ReportApiService,
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

    // 전체 소음 기록 수 (서버 total-count)
    private val _totalNoiseRecordCount = MutableStateFlow<Int?>(null)
    val totalNoiseRecordCount: StateFlow<Int?> = _totalNoiseRecordCount.asStateFlow()

    // 이번 달 소음 기록 수 (서버 monthly-count)
    private val _monthlyNoiseRecordCount = MutableStateFlow<Int?>(null)
    val monthlyNoiseRecordCount: StateFlow<Int?> = _monthlyNoiseRecordCount.asStateFlow()

    // 평균 데시벨 (서버 average-db)
    private val _averageNoiseDb = MutableStateFlow<Double?>(null)
    val averageNoiseDb: StateFlow<Double?> = _averageNoiseDb.asStateFlow()

    // 월간 캘린더: 소음 로그가 있는 날짜들
    private val _noiseLogDatesInMonth = MutableStateFlow<Set<LocalDate>>(emptySet())
    val noiseLogDatesInMonth: StateFlow<Set<LocalDate>> = _noiseLogDatesInMonth.asStateFlow()

    init {
        loadAllLogs() // ViewModel 생성 시 전체 일기 로드
        fetchTotalNoiseRecordCount()
        fetchMonthlyNoiseRecordCount()
        fetchAverageNoiseDb()

        // 초기 진입 시 현재 연/월 기준 캘린더 로드
        val cal = java.util.Calendar.getInstance()
        fetchMonthlyCalendar(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1)
    }

    // 모든 일기 로드 - Repository에서 서버 또는 로컬 DB 데이터 가져옴
    fun loadAllLogs() {
        viewModelScope.launch {
            repository.getAllNoiseLogs().onSuccess { logs ->
                _noiseLogs.value = logs
            }.onFailure { error ->
            }
        }
    }

    // 날짜 선택 및 해당 날짜의 일기 로드 - 서버 일별 캘린더 API 사용 + 시간순 정렬
    fun selectDate(date: Date) {
        _selectedDate.value = date
        viewModelScope.launch {
            val fmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val dateString = fmt.format(date)

            when (val result = safeApiCall { noiseCalendarApiService.getDailyCalendar(dateString) }) {
                is NetworkResult.Success -> {
                    val logs = result.data.data.records
                        .sortedBy { LocalDateTime.parse(it.occuredAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
                        .map { it.toDomain() }
                    _selectedLogs.value = logs
                }
                is NetworkResult.Error -> {
                    repository.getNoiseLogsByDate(date).onSuccess { logs ->
                        _selectedLogs.value = logs.sortedBy { it.measuredAt }
                    }.onFailure {
                        _selectedLogs.value = emptyList()
                    }
                }
            }
        }
    }

    // 일기 삭제 - Repository를 통해 서버와 로컬 DB에서 삭제
    fun deleteLog(id: String) {
        viewModelScope.launch {
            val longId = id.toLongOrNull() ?: return@launch
            when (safeApiCall { noiseRecordApiService.deleteNoiseRecord(longId) }) {
                is NetworkResult.Success -> {
                    repository.deleteNoiseLog(id)
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {}
            }
        }
    }

    // 리포트 상태 토글 - 실제로는 별도의 리포트 생성 API 호출 필요
    fun toggleReportStatus(log: NoiseLog) {
        viewModelScope.launch {
            val updated = log.copy(hasReport = !log.hasReport)
            repository.updateNoiseLog(updated).onSuccess {
                loadAllLogs()
                selectDate(_selectedDate.value)
            }.onFailure { error ->
            }
        }
    }

    // 새로운 소음 일기 저장 - Repository를 통해 서버에 저장
    fun saveLog(log: NoiseLog) {
        viewModelScope.launch {
            val req = CreateNoiseRecordReq(
                occuredAt = log.measuredAt.toIsoStringZ(),
                duration = 0, // TODO: duration 정보 없어서 0으로 전달
                dbHigh = log.maxDecibel,
                dbAvg = log.avgDecibel,
                category = mapCategory(log.noiseType),
                grade = mapGrade(log.avgDecibel),
                description = log.memo
            )

            when (val result = safeApiCall { noiseRecordApiService.createNoiseRecord(req) }) {
                is NetworkResult.Success -> {
                    // 로컬 목록도 갱신해 화면 반영
                    repository.saveNoiseLog(log)
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {
                    // 실패 시 기존 로컬 저장 로직 유지(옵션)
                }
            }
        }
    }

    // 소음 일기 수정 - Repository를 통해 서버의 데이터 업데이트
    fun updateLog(log: NoiseLog) {
        viewModelScope.launch {
            val id = log.id?.toLongOrNull() ?: return@launch
            val req = ModifyNoiseRecordReq(
                category = mapCategory(log.noiseType),
                occuredAt = log.measuredAt.toIsoStringNoZ(),
                noiseGrade = mapGrade(log.avgDecibel),
                dbHigh = log.maxDecibel,
                dbAvg = log.avgDecibel,
                summary = log.memo
            )

            when (safeApiCall { noiseRecordApiService.modifyNoiseRecord(id, req) }) {
                is NetworkResult.Success -> {
                    repository.updateNoiseLog(log)
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {}
            }
        }
    }

    // 서버 전체 소음 기록 수 조회
    fun fetchTotalNoiseRecordCount() {
        viewModelScope.launch {
            when (val result = safeApiCall { noiseRecordApiService.getAllNoiseRecordCount() }) {
                is NetworkResult.Success -> _totalNoiseRecordCount.value = result.data.totalCount
                is NetworkResult.Error -> _totalNoiseRecordCount.value = null
            }
        }
    }

    // 서버 이번 달 소음 기록 수 조회
    fun fetchMonthlyNoiseRecordCount(year: Int? = null, month: Int? = null) {
        viewModelScope.launch {
            val cal = java.util.Calendar.getInstance()
            val targetYear = year ?: cal.get(java.util.Calendar.YEAR)
            val targetMonth = month ?: (cal.get(java.util.Calendar.MONTH) + 1) // Calendar.MONTH는 0부터 시작

            when (val result = safeApiCall {
                noiseRecordApiService.getMonthlyNoiseRecordCount(
                    year = targetYear,
                    month = targetMonth
                )
            }) {
                is NetworkResult.Success -> _monthlyNoiseRecordCount.value = result.data.monthlyCount
                is NetworkResult.Error -> _monthlyNoiseRecordCount.value = null
            }
        }
    }

    // 서버 평균 데시벨 조회
    fun fetchAverageNoiseDb() {
        viewModelScope.launch {
            when (val result = safeApiCall { noiseRecordApiService.getAverageNoiseDB() }) {
                is NetworkResult.Success -> _averageNoiseDb.value = result.data.averageDb
                is NetworkResult.Error -> _averageNoiseDb.value = null
            }
        }
    }

    // 월간 캘린더 API 연동
    fun fetchMonthlyCalendar(year: Int, month: Int) {
        viewModelScope.launch {
            when (val result = safeApiCall { noiseCalendarApiService.getMonthCalendar(year, month) }) {
                is NetworkResult.Success -> {
                    val set = result.data.data.dates
                        .filter { it.hasNoiseLog }
                        .mapNotNull { day ->
                            try {
                                LocalDate.parse(day.date)
                            } catch (_: Exception) {
                                null
                            }
                        }.toSet()
                    _noiseLogDatesInMonth.value = set
                }
                is NetworkResult.Error -> {
                    _noiseLogDatesInMonth.value = emptySet()
                }
            }
        }
    }

    // 리포트 생성
    fun createReport(selectedIds: List<String>) {
        viewModelScope.launch {
            val ids = selectedIds.mapNotNull { it.toLongOrNull() }
            if (ids.isEmpty()) return@launch

            when (safeApiCall { reportApiService.createReport(CreateReportReq(recordIds = ids)) }) {
                is NetworkResult.Success -> {
                    // 리포트 생성 후 헤더/리스트 갱신
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {
                    // TODO: 에러 처리 (스낵바/토스트)
                }
            }
        }
    }

    private fun refreshHeaderCounters() {
        fetchTotalNoiseRecordCount()
        fetchMonthlyNoiseRecordCount()
        fetchAverageNoiseDb()
        val cal = java.util.Calendar.getInstance()
        fetchMonthlyCalendar(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1)
    }

    private fun mapCategory(noiseType: String): String = when (noiseType.uppercase()) {
        "FOOTSTEPS", "발걸음" -> "FOOTSTEPS"
        "HAMMERING", "망치질" -> "HAMMERING"
        "FURNITURE", "가구" -> "FURNITURE"
        "MUSIC", "음악" -> "MUSIC"
        else -> "UNKNOWN"
    }

    private fun mapGrade(avg: Double): String = when {
        avg >= 65 -> "LOUD"
        avg >= 45 -> "NORMAL"
        else -> "QUIET"
    }

    private fun Date.toIsoStringZ(): String =
        Instant.ofEpochMilli(time).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    private fun Date.toIsoStringNoZ(): String =
        Instant.ofEpochMilli(time).atOffset(ZoneOffset.systemDefault().rules.getOffset(Instant.ofEpochMilli(time)))
            .toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    private fun DailyCalendarRecord.toDomain(): NoiseLog {
        val occured = LocalDateTime.parse(occuredAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val measuredDate = Date.from(occured.atZone(ZoneOffset.systemDefault()).toInstant())
        return NoiseLog(
            id = recordId.toString(),
            noiseType = category,
            maxDecibel = dbHigh.toDouble(),
            avgDecibel = dbAvg.toDouble(),
            memo = summary ?: "",
            measuredAt = measuredDate,
            hasReport = false
        )
    }
}
