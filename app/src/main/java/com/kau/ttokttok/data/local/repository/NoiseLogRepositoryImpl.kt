package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok.data.remote.api.NoiseApiService
import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

// 소음 일기 Repository 구현
// - 로컬 임시 저장: saveNoiseLog, updateNoiseLog, deleteNoiseLog, getAllNoiseLogs, getNoiseLogsByDate
// - 백엔드 API 연동: getCalendarData, getNoiseRecordsByDate, getTotalCount, getMonthlyCount, getAverageDb, updateNoiseRecordApi
class NoiseLogRepositoryImpl @Inject constructor(
    private val noiseApiService: NoiseApiService,
    private val tokenProvider: TokenProvider
) : NoiseLogRepository {

    // 임시 데이터 (로컬 메모리 저장 - 앱 재시작 시 사라짐)
    private val tempLogs = mutableListOf<NoiseLog>()

    // 로컬 임시 저장
    override suspend fun saveNoiseLog(noiseLog: NoiseLog): Result<NoiseLog> {
        return try {
            val newLog = noiseLog.copy(id = System.currentTimeMillis().toString())
            tempLogs.add(newLog)
            Result.success(newLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 로컬 임시 수정
    override suspend fun updateNoiseLog(noiseLog: NoiseLog): Result<NoiseLog> {
        return try {
            val index = tempLogs.indexOfFirst { it.id == noiseLog.id }
            if (index != -1) {
                tempLogs[index] = noiseLog
                Result.success(noiseLog)
            } else {
                Result.failure(Exception("일기를 찾을 수 없습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 로컬 임시 삭제
    override suspend fun deleteNoiseLog(id: String): Result<Unit> {
        return try {
            tempLogs.removeIf { it.id == id }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 로컬 임시 조회
    override suspend fun getAllNoiseLogs(): Result<List<NoiseLog>> {
        return try {
            Result.success(tempLogs.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 로컬 임시 조회 (날짜별 필터링)
    override suspend fun getNoiseLogsByDate(date: Date): Result<List<NoiseLog>> {
        return try {
            val calendar = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val targetYear = calendar.get(Calendar.YEAR)
            val targetMonth = calendar.get(Calendar.MONTH)
            val targetDay = calendar.get(Calendar.DAY_OF_MONTH)

            val filtered = tempLogs.filter { log ->
                val logCalendar = Calendar.getInstance().apply {
                    time = log.measuredAt
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                logCalendar.get(Calendar.YEAR) == targetYear &&
                logCalendar.get(Calendar.MONTH) == targetMonth &&
                logCalendar.get(Calendar.DAY_OF_MONTH) == targetDay
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // [백엔드 API 연동] 캘린더 월별 데이터 조회
    override suspend fun getCalendarData(
        userId: Long,
        year: Int,
        month: Int
    ): Result<Map<String, Boolean>> {
        return try {
            val token = tokenProvider.getTokenOrNull() ?: return Result.failure(Exception("인증 토큰이 없습니다"))

            val response = noiseApiService.getCalendarData(
                userId = userId,
                year = year,
                month = month,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                val calendarResponse = response.body()!!

                if (calendarResponse.isSuccess) {
                    // API 응답을 Map으로 변환 (날짜 -> hasNoiseLog)
                    val calendarMap = calendarResponse.result.data.associate { dayData ->
                        dayData.date to dayData.hasNoiseLog
                    }
                    Result.success(calendarMap)
                } else {
                    Result.failure(Exception(calendarResponse.message))
                }
            } else {
                Result.failure(Exception("서버 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // [백엔드 API 연동] 날짜별 소음 기록 상세 조회
    override suspend fun getNoiseRecordsByDate(date: String): Result<List<NoiseLog>> {
        return try {
            val token = tokenProvider.getTokenOrNull() ?: return Result.failure(Exception("인증 토큰이 없습니다"))

            val response = noiseApiService.getNoiseRecordsByDate(
                date = date,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                val recordsResponse = response.body()!!

                if (recordsResponse.isSuccess) {
                    // API 응답을 NoiseLog 도메인 모델로 변환
                    val noiseLogs = recordsResponse.result.records.map { record ->
                        NoiseLog(
                            id = record.recordId.toString(),
                            noiseType = record.noiseType,
                            maxDecibel = record.noiseLevel.toDouble(),
                            avgDecibel = record.noiseLevel.toDouble(),
                            memo = record.memo ?: "",
                            measuredAt = parseDateTime(record.logDate, record.logTime),
                            hasReport = false // 기본값
                        )
                    }
                    Result.success(noiseLogs)
                } else {
                    Result.failure(Exception(recordsResponse.message))
                }
            } else {
                Result.failure(Exception("서버 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 날짜와 시간 문자열을 Date 객체로 변환
    private fun parseDateTime(dateStr: String, timeStr: String): Date {
        return try {
            val dateTimeStr = "$dateStr $timeStr"
            val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            format.parse(dateTimeStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    // [백엔드 API 연동] 총 소음 기록 수 조회
    override suspend fun getTotalCount(userId: Long): Result<Int> {
        return try {
            val token = tokenProvider.getTokenOrNull() ?: return Result.failure(Exception("인증 토큰이 없습니다"))

            val response = noiseApiService.getTotalCount(
                userId = userId,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                val totalCountResponse = response.body()!!

                if (totalCountResponse.isSuccess) {
                    Result.success(totalCountResponse.result.totalCount)
                } else {
                    Result.failure(Exception(totalCountResponse.message))
                }
            } else {
                Result.failure(Exception("서버 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // [백엔드 API 연동] 이번 달 소음 기록 수 조회
    override suspend fun getMonthlyCount(userId: Long, year: Int, month: Int): Result<Int> {
        return try {
            val token = tokenProvider.getTokenOrNull() ?: return Result.failure(Exception("인증 토큰이 없습니다"))

            val response = noiseApiService.getMonthlyCount(
                userId = userId,
                year = year,
                month = month,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                val monthlyCountResponse = response.body()!!

                if (monthlyCountResponse.isSuccess) {
                    Result.success(monthlyCountResponse.result.monthlyCount)
                } else {
                    Result.failure(Exception(monthlyCountResponse.message))
                }
            } else {
                Result.failure(Exception("서버 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // [백엔드 API 연동] 전체 소음 기록 평균 dB 조회
    override suspend fun getAverageDb(userId: Long): Result<Double> {
        return try {
            val token = tokenProvider.getTokenOrNull() ?: return Result.failure(Exception("인증 토큰이 없습니다"))

            val response = noiseApiService.getAverageDb(
                userId = userId,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                val averageDbResponse = response.body()!!

                if (averageDbResponse.isSuccess) {
                    Result.success(averageDbResponse.result.averageDb)
                } else {
                    Result.failure(Exception(averageDbResponse.message))
                }
            } else {
                Result.failure(Exception("서버 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // [백엔드 API 연동] 소음 일기 수정
    override suspend fun updateNoiseRecordApi(recordId: Long, noiseLog: NoiseLog): Result<NoiseLog> {
        return try {
            val token = tokenProvider.getTokenOrNull() ?: return Result.failure(Exception("인증 토큰이 없습니다"))

            // NoiseLog를 API 요청 DTO로 변환
            val request = com.kau.ttokttok.data.remote.dto.UpdateNoiseRecordRequest(
                category = noiseLog.noiseType,
                occuredAt = formatDateTimeForApi(noiseLog.measuredAt),
                noiseGrade = convertToNoiseGrade(noiseLog.maxDecibel),
                dbHigh = noiseLog.maxDecibel,
                dbAvg = noiseLog.avgDecibel,
                summary = noiseLog.memo
            )

            val response = noiseApiService.updateNoiseRecord(
                recordId = recordId,
                request = request,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                val updateResponse = response.body()!!

                if (updateResponse.isSuccess) {
                    // API 응답을 NoiseLog 도메인 모델로 변환
                    val updatedLog = NoiseLog(
                        id = updateResponse.result.noiseId.toString(),
                        noiseType = updateResponse.result.category,
                        maxDecibel = updateResponse.result.dbHigh,
                        avgDecibel = updateResponse.result.dbAvg,
                        memo = updateResponse.result.summary ?: "",
                        measuredAt = parseDateTimeFromApi(updateResponse.result.occuredAt),
                        hasReport = noiseLog.hasReport
                    )
                    Result.success(updatedLog)
                } else {
                    Result.failure(Exception(updateResponse.message))
                }
            } else {
                Result.failure(Exception("서버 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Date 객체를 API용 ISO 8601 형식으로 변환 (yyyy-MM-ddTHH:mm:ss)
    private fun formatDateTimeForApi(date: Date): String {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        return format.format(date)
    }

    // API의 ISO 8601 형식 문자열을 Date 객체로 변환
    private fun parseDateTimeFromApi(dateTimeStr: String): Date {
        return try {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            format.parse(dateTimeStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    // dB 값을 NoiseGrade로 변환
    private fun convertToNoiseGrade(dbHigh: Double): String {
        return when {
            dbHigh >= 80 -> "VERY_LOUD"
            dbHigh >= 60 -> "LOUD"
            dbHigh >= 40 -> "NORMAL"
            else -> "QUIET"
        }
    }
}
