package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.NoiseLog
import java.util.Date

/**
 * 소음 일기 Repository
 * TODO: [백엔드 연동] 실제 API 연동 시 구현체를 RemoteDataSource로 교체
 * TODO: [백엔드 연동] Repository 패턴으로 Local(Room) + Remote(Retrofit) 동시 사용 고려
 */
interface NoiseLogRepository {

    // TODO: [백엔드 연동] 서버 저장 성공 시 로컬 DB에도 캐싱
    // 일기 저장
    suspend fun saveNoiseLog(noiseLog: NoiseLog): Result<NoiseLog>

    // TODO: [백엔드 연동] 서버 수정 후 로컬 DB 동기화
    // 일기 수정
    suspend fun updateNoiseLog(noiseLog: NoiseLog): Result<NoiseLog>

    // TODO: [백엔드 연동] 서버 삭제 후 로컬 DB에서도 제거
    // 일기 삭제
    suspend fun deleteNoiseLog(id: String): Result<Unit>

    // TODO: [백엔드 연동] 첫 로드는 서버에서, 이후 로컬 캐시 활용
    // 모든 일기 가져오기
    suspend fun getAllNoiseLogs(): Result<List<NoiseLog>>

    // TODO: [백엔드 연동] 서버에서 날짜 범위로 쿼리
    // 날짜별 일기 가져오기
    suspend fun getNoiseLogsByDate(date: Date): Result<List<NoiseLog>>
}
