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
import android.util.Log

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

    // UI에 한 번만 보여줄 메시지 (Toast 등)
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

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
            val cal = java.util.Calendar.getInstance().apply { time = date }
            val year = cal.get(java.util.Calendar.YEAR)
            val month = cal.get(java.util.Calendar.MONTH) + 1 // Calendar.MONTH는 0부터
            val day = cal.get(java.util.Calendar.DAY_OF_MONTH)

            when (val result = safeApiCall {
                noiseCalendarApiService.getDailyCalendar(
                    year = year,
                    month = month,
                    day = day
                )
            }) {
                is NetworkResult.Success -> {
                    val logs = result.data.data.records
                        .sortedBy { LocalDateTime.parse(it.occuredAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
                        .map { it.toDomain() }
                    _selectedLogs.value = logs
                }
                is NetworkResult.Error -> {
                    // 해당 날짜에 소음 기록이 없을 때 서버에서 NOISE4002 코드 반환
                    if (result.code == "NOISE4002") {
                        _selectedLogs.value = emptyList()
                        _uiMessage.value = "이 날짜에는 소음일기가 없습니다."
                        return@launch
                    }

                    Log.e(
                        "NoiseLogViewModel",
                        "getDailyCalendar 실패 - code=${result.code}, message=${result.message}",
                        result.exception
                    )

                    repository.getNoiseLogsByDate(date).onSuccess { logs ->
                        _selectedLogs.value = logs.sortedBy { it.measuredAt }
                    }.onFailure {
                        _selectedLogs.value = emptyList()
                    }
                }
            }
        }
    }

    // UI에서 메시지를 소비한 뒤 호출하여 한 번만 보여지도록 초기화
    fun consumeUiMessage() {
        _uiMessage.value = null
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
                duration = log.duration.toInt(), // 측정 duration(초)을 서버로 전달
                dbHigh = log.maxDecibel,
                dbAvg = log.avgDecibel,
                category = mapCategory(log.noiseType),
                grade = mapGrade(log.avgDecibel),
                description = log.memo,
                summary = log.memo   // 등록 시에도 summary에 메모를 함께 전달
            )

            when (val result = safeApiCall { noiseRecordApiService.createNoiseRecord(req) }) {
                is NetworkResult.Success -> {
                    // 서버 응답 확인용 로그
                    Log.d(
                        "NoiseLogViewModel",
                        "createNoiseRecord 성공 - id=${result.data.id}, description=${result.data.description}, summary=${result.data.summary}"
                    )

                    // 서버가 등록 시 description만 저장하고 summary는 null로 주는 문제 해결:
                    // 즉시 수정 API를 호출해서 summary에도 메모를 저장
                    if (result.data.summary.isNullOrBlank() && !log.memo.isNullOrBlank()) {
                        val patchReq = ModifyNoiseRecordReq(
                            category = mapCategory(log.noiseType),
                            occuredAt = log.measuredAt.toIsoStringZ(),
                            noiseGrade = mapGrade(log.avgDecibel),
                            dbHigh = log.maxDecibel,
                            dbAvg = log.avgDecibel,
                            summary = log.memo
                        )

                        // 백그라운드로 수정 API 호출 (실패해도 무시)
                        viewModelScope.launch {
                            safeApiCall { noiseRecordApiService.modifyNoiseRecord(result.data.id, patchReq) }
                            Log.d("NoiseLogViewModel", "등록 직후 summary 업데이트 완료")
                        }
                    }

                    // 로컬 목록도 갱신해 화면 반영
                    repository.saveNoiseLog(log)
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {
                    Log.e(
                        "NoiseLogViewModel",
                        "createNoiseRecord 실패 - code=${result.code}, message=${result.message}",
                        result.exception
                    )
                    _uiMessage.value = result.message ?: "소음 일기 저장에 실패했습니다. 잠시 후 다시 시도해 주세요."
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
                // 생성 API와 동일하게 UTC 기준 ISO_OFFSET_DATE_TIME 포맷 사용
                occuredAt = log.measuredAt.toIsoStringZ(),
                noiseGrade = mapGrade(log.avgDecibel),
                dbHigh = log.maxDecibel,
                dbAvg = log.avgDecibel,
                summary = log.memo
            )

            when (val result = safeApiCall { noiseRecordApiService.modifyNoiseRecord(id, req) }) {
                is NetworkResult.Success -> {
                    repository.updateNoiseLog(log)
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {
                    Log.e(
                        "NoiseLogViewModel",
                        "modifyNoiseRecord 실패 - code=${result.code}, message=${result.message}",
                        result.exception
                    )
                    _uiMessage.value = result.message ?: "소음 일기 수정에 실패했습니다. 잠시 후 다시 시도해 주세요."
                }
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
            // 디버그용: 실제로 서버로 나가는 year/month를 로그로 확인
            Log.d("NoiseLogViewModel", "fetchMonthlyCalendar: year=$year, month=$month")

            // 방어 로직: 서버는 1~12 범위의 month만 허용한다고 가정하고, 범위를 벗어나면 호출하지 않음
            if (month !in 1..12) {
                Log.e(
                    "NoiseLogViewModel",
                    "fetchMonthlyCalendar called with invalid month=$month (year=$year). 서버 호출을 생략합니다."
                )
                _noiseLogDatesInMonth.value = emptySet()
                return@launch
            }

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
                    Log.e(
                        "NoiseLogViewModel",
                        "getMonthCalendar 실패 - code=${result.code}, message=${result.message}, year=$year, month=$month",
                        result.exception
                    )
                    _noiseLogDatesInMonth.value = emptySet()
                }
            }
        }
    }

    // 리포트 생성
    fun createReport(selectedIds: List<String>) {
        viewModelScope.launch {
            val ids = selectedIds.mapNotNull { it.toLongOrNull() }
            if (ids.isEmpty()) {
                Log.w("NoiseLogViewModel", "createReport 호출됐지만 유효한 ID가 없음")
                return@launch
            }

            Log.d("NoiseLogViewModel", "createReport 요청 - recordIds=${ids.joinToString()}")

            when (val result = safeApiCall { reportApiService.createReport(CreateReportReq(recordIds = ids)) }) {
                is NetworkResult.Success -> {
                    Log.d(
                        "NoiseLogViewModel",
                        "createReport 성공 - reportId=${result.data.reportId}, pdfUrl=${result.data.pdfUrl}"
                    )
                    _uiMessage.value = "리포트가 소음현황 페이지로 전송되었습니다"

                    // 리포트 생성 후 헤더/리스트 갱신
                    refreshHeaderCounters()
                    selectDate(_selectedDate.value)
                }
                is NetworkResult.Error -> {
                    Log.e(
                        "NoiseLogViewModel",
                        "createReport 실패 - code=${result.code}, message=${result.message}",
                        result.exception
                    )

                    // HTTP 404는 서버 API 미구현 또는 경로 오류
                    val errorMessage = when {
                        result.message?.contains("404") == true ->
                            "리포트 전송 기능이 아직 준비되지 않았습니다. 서버 담당자에게 문의해 주세요."
                        else ->
                            result.message ?: "리포트 전송에 실패했습니다. 잠시 후 다시 시도해 주세요."
                    }
                    _uiMessage.value = errorMessage
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

        // 메모 우선순위: summary(수정 시) > description(등록 시) > 빈 문자열
        val memoText = when {
            !summary.isNullOrBlank() -> summary
            !description.isNullOrBlank() -> description
            else -> ""
        }

        return NoiseLog(
            id = recordId.toString(),
            noiseType = category,
            maxDecibel = dbHigh.toDouble(),
            avgDecibel = dbAvg.toDouble(),
            memo = memoText,
            measuredAt = measuredDate,
            hasReport = false
        )
    }
}
