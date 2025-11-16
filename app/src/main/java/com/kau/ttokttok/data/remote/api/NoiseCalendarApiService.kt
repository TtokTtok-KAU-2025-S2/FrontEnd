package com.kau.ttokttok.data.remote.api

import com.kau.ttokttok._core.network.model.ApiResponse
import com.kau.ttokttok.data.remote.dto.noisecalendar.res.GetDailyCalendarRes
import com.kau.ttokttok.data.remote.dto.noisecalendar.res.GetMonthlyNoiseCalendarRes
import retrofit2.http.GET
import retrofit2.http.Query

interface NoiseCalendarApiService {
    // TODO: 아래 메소드들은 BE 준비중
    @GET("noise/records/calendar")
    suspend fun getMonthCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<GetMonthlyNoiseCalendarRes>

    @GET("noise/records/calendar")
    suspend fun getDailyCalendar(
        @Query("date") date: String
    ): ApiResponse<GetDailyCalendarRes>
}