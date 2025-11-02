package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.NoiseLog
import java.util.Date

/**
 * 소음 일기 Repository
 */
interface NoiseLogRepository {

    // 일기 저장
    suspend fun saveNoiseLog(noiseLog: NoiseLog): Result<NoiseLog>

    // 일기 수정
    suspend fun updateNoiseLog(noiseLog: NoiseLog): Result<NoiseLog>

    // 일기 삭제
    suspend fun deleteNoiseLog(id: String): Result<Unit>

    // 모든 일기 가져오기
    suspend fun getAllNoiseLogs(): Result<List<NoiseLog>>

    // 날짜별 일기 가져오기
    suspend fun getNoiseLogsByDate(date: Date): Result<List<NoiseLog>>
}
