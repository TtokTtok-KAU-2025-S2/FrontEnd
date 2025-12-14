package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.noisecalendar.res.*
import retrofit2.http.GET
import retrofit2.http.Query

interface NoiseCalendarApiService {
    // 월간 캘린더: year, month 기준 조회
    @GET("noise/records/calendar")
    suspend fun getMonthCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<GetMonthlyNoiseCalendarRes>

    // 일자별 소음 일기 조회: 변경된 명세에 따라 year, month, day 쿼리 사용
    @GET("noise/records/calendar/details")
    suspend fun getDailyCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): ApiResponse<GetDailyCalendarRes>
}