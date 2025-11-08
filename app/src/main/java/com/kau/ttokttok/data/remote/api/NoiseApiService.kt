package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok.data.remote.dto.AverageDbResponse
import com.kau.ttokttok.data.remote.dto.CalendarResponse
import com.kau.ttokttok.data.remote.dto.MonthlyCountResponse
import com.kau.ttokttok.data.remote.dto.NoiseRecordsByDateResponse
import com.kau.ttokttok.data.remote.dto.TotalCountResponse
import com.kau.ttokttok.data.remote.dto.UpdateNoiseRecordRequest
import com.kau.ttokttok.data.remote.dto.UpdateNoiseRecordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 소음 일기 관련 API 서비스
 */
interface NoiseApiService {

    /**
     * 캘린더 월별 소음 일기 조회
     * GET /noise/records/calendar/{userId}
     *
     * @param userId 회원 고유 ID
     * @param year 조회할 연도 (예: 2025)
     * @param month 조회할 월 (예: 10)
     * @param authorization JWT 토큰 ("Bearer {token}")
     * @return 해당 월의 모든 날짜와 소음 일기 존재 여부
     */
    @GET("noise/records/calendar/{userId}")
    suspend fun getCalendarData(
        @Path("userId") userId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Header("Authorization") authorization: String
    ): Response<CalendarResponse>

    /**
     * 날짜별 소음 기록 상세 조회
     * GET /noise/records/calendar?date=2025-10-27
     *
     * @param date 조회할 날짜 (yyyy-MM-dd 형식)
     * @param authorization JWT 토큰 ("Bearer {token}")
     * @return 해당 날짜의 소음 기록 목록 (생성 시간 순 정렬)
     */
    @GET("noise/records/calendar")
    suspend fun getNoiseRecordsByDate(
        @Query("date") date: String,
        @Header("Authorization") authorization: String
    ): Response<NoiseRecordsByDateResponse>

    /**
     * 총 소음 기록 수 조회
     * GET /noise/records/total-count/{userId}
     *
     * @param userId 조회할 사용자의 ID
     * @param authorization JWT 토큰 ("Bearer {token}")
     * @return 사용자의 전체 소음 기록 개수
     */
    @GET("noise/records/total-count/{userId}")
    suspend fun getTotalCount(
        @Path("userId") userId: Long,
        @Header("Authorization") authorization: String
    ): Response<TotalCountResponse>

    /**
     * 이번 달 소음 기록 수 조회
     * GET /noise/records/monthly-count/{userId}
     *
     * @param userId 조회할 사용자의 ID
     * @param year 조회할 연도
     * @param month 조회할 월
     * @param authorization JWT 토큰 ("Bearer {token}")
     * @return 해당 월의 소음 기록 개수
     */
    @GET("noise/records/monthly-count/{userId}")
    suspend fun getMonthlyCount(
        @Path("userId") userId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Header("Authorization") authorization: String
    ): Response<MonthlyCountResponse>

    /**
     * 전체 소음 기록 평균 dB 조회
     * GET /noise/records/average-db/{userId}
     *
     * @param userId 조회할 사용자의 ID
     * @param authorization JWT 토큰 ("Bearer {token}")
     * @return 사용자의 전체 소음 기록 평균 dB
     */
    @GET("noise/records/average-db/{userId}")
    suspend fun getAverageDb(
        @Path("userId") userId: Long,
        @Header("Authorization") authorization: String
    ): Response<AverageDbResponse>

    /**
     * 소음 일기 수정
     * PATCH /noise/records/{recordId}
     *
     * @param recordId 수정할 소음 기록의 고유 ID
     * @param request 수정할 소음 기록 데이터
     * @param authorization JWT 토큰 ("Bearer {token}")
     * @return 수정된 소음 기록 정보
     */
    @PATCH("noise/records/{recordId}")
    suspend fun updateNoiseRecord(
        @Path("recordId") recordId: Long,
        @Body request: UpdateNoiseRecordRequest,
        @Header("Authorization") authorization: String
    ): Response<UpdateNoiseRecordResponse>
}
