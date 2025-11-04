package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import java.util.Calendar
import java.util.Date

/**
 * 소음 일기 Repository 구현
 * TODO: [백엔드 연동] 현재는 로컬 메모리에만 저장되며, 앱 재시작 시 데이터가 사라집니다.
 * TODO: [백엔드 연동] 실제 API 연동 시 아래 작업이 필요합니다:
 * 1. Retrofit 인터페이스 정의 (NoiseLogApiService)
 * 2. API 엔드포인트 설정
 *    - POST /api/noise-logs (소음일기 저장)
 *    - PUT /api/noise-logs/{id} (소음일기 수정)
 *    - DELETE /api/noise-logs/{id} (소음일기 삭제)
 *    - GET /api/noise-logs (전체 소음일기 조회)
 *    - GET /api/noise-logs/date/{date} (날짜별 소음일기 조회)
 * 3. 인증 토큰 헤더 추가
 * 4. 에러 핸들링 (네트워크 오류, 인증 실패 등)
 * 5. 오프라인 대응 (Room DB와 동기화)
 */
class NoiseLogRepositoryImpl : NoiseLogRepository {

    // TODO: [백엔드 연동] Retrofit API Service 인스턴스로 교체
    // private val apiService: NoiseLogApiService

    // 임시 데이터 (나중에 실제 API로 교체)
    private val tempLogs = mutableListOf<NoiseLog>()

    // TODO: [백엔드 연동] API 호출로 변경
    // 예시: apiService.createNoiseLog(noiseLog.toDto())
    override suspend fun saveNoiseLog(noiseLog: NoiseLog): Result<NoiseLog> {
        return try {
            // TODO: [백엔드 연동] 서버에서 생성된 ID를 받아와야 함
            val newLog = noiseLog.copy(id = System.currentTimeMillis().toString())
            tempLogs.add(newLog)
            Result.success(newLog)
        } catch (e: Exception) {
            // TODO: [백엔드 연동] 네트워크 에러 처리 추가
            Result.failure(e)
        }
    }

    // TODO: [백엔드 연동] API 호출로 변경
    // 예시: apiService.updateNoiseLog(noiseLog.id, noiseLog.toDto())
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
            // TODO: [백엔드 연동] 네트워크 에러 처리 추가
            Result.failure(e)
        }
    }

    // TODO: [백엔드 연동] API 호출로 변경
    // 예시: apiService.deleteNoiseLog(id)
    override suspend fun deleteNoiseLog(id: String): Result<Unit> {
        return try {
            tempLogs.removeIf { it.id == id }
            Result.success(Unit)
        } catch (e: Exception) {
            // TODO: [백엔드 연동] 네트워크 에러 처리 추가
            Result.failure(e)
        }
    }

    // TODO: [백엔드 연동] API 호출로 변경
    // 예시: apiService.getAllNoiseLogs().map { it.toDomain() }
    // TODO: [백엔드 연동] 페이징 처리 고려 (데이터가 많을 경우)
    override suspend fun getAllNoiseLogs(): Result<List<NoiseLog>> {
        return try {
            Result.success(tempLogs.toList())
        } catch (e: Exception) {
            // TODO: [백엔드 연동] 네트워크 에러 처리 추가
            Result.failure(e)
        }
    }

    // TODO: [백엔드 연동] API 호출로 변경
    // 예시: apiService.getNoiseLogsByDate(date.toIsoString())
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

            // TODO: [백엔드 연동] 서버에서 날짜 필터링된 데이터를 받아오도록 변경
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
            // TODO: [백엔드 연동] 네트워크 에러 처리 추가
            Result.failure(e)
        }
    }
}
