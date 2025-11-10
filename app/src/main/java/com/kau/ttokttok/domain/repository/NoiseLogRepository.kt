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

    /**
     * 캘린더 월별 데이터 조회
     * @param userId 회원 고유 ID
     * @param year 조회할 연도
     * @param month 조회할 월
     * @return 해당 월의 날짜별 소음 일기 존재 여부 Map (날짜 -> hasNoiseLog)
     */
    suspend fun getCalendarData(
        userId: Long,
        year: Int,
        month: Int
    ): Result<Map<String, Boolean>>

    /**
     * 날짜별 소음 기록 상세 조회
     * @param date 조회할 날짜 (yyyy-MM-dd 형식)
     * @return 해당 날짜의 소음 기록 목록 (생성 시간 순 정렬)
     */
    suspend fun getNoiseRecordsByDate(
        date: String
    ): Result<List<NoiseLog>>

    /**
     * 총 소음 기록 수 조회
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 전체 소음 기록 개수
     */
    suspend fun getTotalCount(
        userId: Long
    ): Result<Int>

    /**
     * 이번 달 소음 기록 수 조회
     * @param userId 조회할 사용자의 ID
     * @param year 조회할 연도
     * @param month 조회할 월
     * @return 해당 월의 소음 기록 개수
     */
    suspend fun getMonthlyCount(
        userId: Long,
        year: Int,
        month: Int
    ): Result<Int>

    /**
     * 전체 소음 기록 평균 dB 조회
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 전체 소음 기록 평균 dB
     */
    suspend fun getAverageDb(
        userId: Long
    ): Result<Double>

    /**
     * 소음 일기 등록 (API 연동)
     * @param occuredAt 소음 발생 시간 (ISO 8601 형식)
     * @param duration 측정 소요 시간(녹음 duration)
     * @param dbHigh 최대 데시벨
     * @param dbAvg 평균 데시벨
     * @param category 소음 카테고리 (FOOTSTEPS, HAMMERING, FURNITURE, MUSIC, UNKNOWN)
     * @param grade 소음 등급 (QUIET, NORMAL, LOUD)
     * @param description 소음일기 내용
     * @return 생성된 소음 일기
     */
    suspend fun createNoiseRecordApi(
        occuredAt: String,
        duration: Int,
        dbHigh: Double,
        dbAvg: Double,
        category: String,
        grade: String,
        description: String?
    ): Result<NoiseLog>

    /**
     * 소음 일기 수정 (API 연동)
     * @param recordId 수정할 소음 기록의 고유 ID
     * @param noiseLog 수정할 소음 일기 데이터
     * @return 수정된 소음 일기
     */
    suspend fun updateNoiseRecordApi(
        recordId: Long,
        noiseLog: NoiseLog
    ): Result<NoiseLog>

    /**
     * 소음 일기 삭제 (API 연동 - Hard Delete)
     * @param recordId 삭제할 소음 기록의 고유 ID
     * @return 삭제된 소음 기록 ID
     */
    suspend fun deleteNoiseRecordApi(
        recordId: Long
    ): Result<Long>

    /**
     * 소음 기록 1개 데이터를 '소음현황' 페이지로 전송 (API 연동)
     * @param recordId 전송할 소음 기록의 고유 ID
     * @return 전송된 소음 기록 ID
     */
    suspend fun sendNoiseRecordApi(
        recordId: Long
    ): Result<Long>
}
