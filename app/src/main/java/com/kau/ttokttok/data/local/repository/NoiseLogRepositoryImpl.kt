package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok.domain.model.NoiseLog
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import java.util.Date
import javax.inject.Inject

/**
 * 소음 일기 Repository 구현
 * TODO: 나중에 실제 API 연동하기
 */
class NoiseLogRepositoryImpl @Inject constructor() : NoiseLogRepository {

    // 임시 데이터 (나중에 실제 API로 교체)
    private val tempLogs = mutableListOf<NoiseLog>()

    override suspend fun saveNoiseLog(noiseLog: NoiseLog): Result<NoiseLog> {
        return try {
            val newLog = noiseLog.copy(id = System.currentTimeMillis().toString())
            tempLogs.add(newLog)
            Result.success(newLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    override suspend fun deleteNoiseLog(id: String): Result<Unit> {
        return try {
            tempLogs.removeIf { it.id == id }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllNoiseLogs(): Result<List<NoiseLog>> {
        return try {
            Result.success(tempLogs.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNoiseLogsByDate(date: Date): Result<List<NoiseLog>> {
        return try {
            val filtered = tempLogs.filter {
                // 같은 날짜인지 비교 (시간 제외)
                it.measuredAt.date == date.date &&
                it.measuredAt.month == date.month &&
                it.measuredAt.year == date.year
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
