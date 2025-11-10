package com.kau.ttokttok.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 캘린더 API 응답 모델
 * GET /noise/records/calendar/{userId} (월별 조회)
 */
@JsonClass(generateAdapter = true)
data class CalendarResponse(
    @Json(name = "result")
    val result: CalendarResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class CalendarResult(
    @Json(name = "month")
    val month: Int,
    @Json(name = "data")
    val data: List<CalendarDayData>,
    @Json(name = "year")
    val year: Int,
    @Json(name = "userId")
    val userId: Long
)

@JsonClass(generateAdapter = true)
data class CalendarDayData(
    @Json(name = "date")
    val date: String, // "2025-10-01" 형식
    @Json(name = "hasNoiseLog")
    val hasNoiseLog: Boolean
)

/**
 * 날짜별 소음 기록 조회 API 응답 모델
 * GET /noise/records/calendar?date=2025-10-27
 */
@JsonClass(generateAdapter = true)
data class NoiseRecordsByDateResponse(
    @Json(name = "result")
    val result: NoiseRecordsByDateResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class NoiseRecordsByDateResult(
    @Json(name = "date")
    val date: String,
    @Json(name = "records")
    val records: List<NoiseRecordDetail>,
    @Json(name = "userId")
    val userId: Long
)

@JsonClass(generateAdapter = true)
data class NoiseRecordDetail(
    @Json(name = "recordId")
    val recordId: Long,
    @Json(name = "logDate")
    val logDate: String,
    @Json(name = "logTime")
    val logTime: String,
    @Json(name = "noiseType")
    val noiseType: String,
    @Json(name = "noiseLevel")
    val noiseLevel: Int,
    @Json(name = "memo")
    val memo: String?,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "modifiedAt")
    val modifiedAt: String
)

/**
 * 총 소음 기록 수 조회 API 응답 모델
 * GET /noise/records/total-count/{userId}
 */
@JsonClass(generateAdapter = true)
data class TotalCountResponse(
    @Json(name = "result")
    val result: TotalCountResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class TotalCountResult(
    @Json(name = "totalCount")
    val totalCount: Int
)

/**
 * 이번 달 소음 기록 수 조회 API 응답 모델
 * GET /noise/records/monthly-count/{userId}
 */
@JsonClass(generateAdapter = true)
data class MonthlyCountResponse(
    @Json(name = "result")
    val result: MonthlyCountResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class MonthlyCountResult(
    @Json(name = "month")
    val month: Int,
    @Json(name = "monthlyCount")
    val monthlyCount: Int,
    @Json(name = "year")
    val year: Int
)

/**
 * 전체 소음 기록 평균 dB 조회 API 응답 모델
 * GET /noise/records/average-db/{userId}
 */
@JsonClass(generateAdapter = true)
data class AverageDbResponse(
    @Json(name = "result")
    val result: AverageDbResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class AverageDbResult(
    @Json(name = "averageDb")
    val averageDb: Double
)

/**
 * 소음 일기 등록 API 요청 모델
 * POST /noise/records
 */
@JsonClass(generateAdapter = true)
data class CreateNoiseRecordRequest(
    @Json(name = "occuredAt")
    val occuredAt: String, // "2025-11-09T11:32:57.272Z" 형식
    @Json(name = "duration")
    val duration: Int, // 측정 소요 시간(녹음 duration)
    @Json(name = "dbHigh")
    val dbHigh: Double, // 최대 데시벨
    @Json(name = "dbAvg")
    val dbAvg: Double, // 평균 데시벨
    @Json(name = "category")
    val category: String, // FOOTSTEPS, HAMMERING, FURNITURE, MUSIC, UNKNOWN
    @Json(name = "grade")
    val grade: String, // QUIET, NORMAL, LOUD
    @Json(name = "description")
    val description: String? // 소음일기 내용
)

/**
 * 소음 일기 등록 API 응답 모델
 * POST /noise/records
 */
@JsonClass(generateAdapter = true)
data class CreateNoiseRecordResponse(
    @Json(name = "result")
    val result: CreateNoiseRecordResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class CreateNoiseRecordResult(
    @Json(name = "id")
    val id: Long,
    @Json(name = "userId")
    val userId: Long,
    @Json(name = "duration")
    val duration: Int,
    @Json(name = "dbHigh")
    val dbHigh: Double,
    @Json(name = "dbAvg")
    val dbAvg: Double,
    @Json(name = "category")
    val category: String,
    @Json(name = "grade")
    val grade: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "summary")
    val summary: String?,
    @Json(name = "occuredAt")
    val occuredAt: String,
    @Json(name = "updateAt")
    val updateAt: String
)

/**
 * 소음 일기 삭제 API 응답 모델
 * DELETE /noise/records/{recordId}
 */
@JsonClass(generateAdapter = true)
data class DeleteNoiseRecordResponse(
    @Json(name = "result")
    val result: DeleteNoiseRecordResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class DeleteNoiseRecordResult(
    @Json(name = "recordId")
    val recordId: Long
)

/**
 * 소음 일기 수정 API 요청 모델
 * PATCH /noise/records/{recordId}
 */
@JsonClass(generateAdapter = true)
data class UpdateNoiseRecordRequest(
    @Json(name = "category")
    val category: String,
    @Json(name = "occuredAt")
    val occuredAt: String, // "2025-11-08T01:34:17" 형식
    @Json(name = "noiseGrade")
    val noiseGrade: String,
    @Json(name = "dbHigh")
    val dbHigh: Double,
    @Json(name = "dbAvg")
    val dbAvg: Double,
    @Json(name = "summary")
    val summary: String?
)

/**
 * 소음 일기 수정 API 응답 모델
 * PATCH /noise/records/{recordId}
 */
@JsonClass(generateAdapter = true)
data class UpdateNoiseRecordResponse(
    @Json(name = "result")
    val result: UpdateNoiseRecordResult,
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

@JsonClass(generateAdapter = true)
data class UpdateNoiseRecordResult(
    @Json(name = "noiseId")
    val noiseId: Long,
    @Json(name = "category")
    val category: String,
    @Json(name = "occuredAt")
    val occuredAt: String,
    @Json(name = "noiseGrade")
    val noiseGrade: String,
    @Json(name = "dbHigh")
    val dbHigh: Double,
    @Json(name = "dbAvg")
    val dbAvg: Double,
    @Json(name = "summary")
    val summary: String?,
    @Json(name = "updatedAt")
    val updatedAt: String
)

/**
 * 소음 기록 전송 API 응답 모델
 * POST /noise/records/{recordId}/send
 */
@JsonClass(generateAdapter = true)
data class SendNoiseRecordResponse(
    @Json(name = "result")
    val result: Long, // 전송된 소음 기록의 ID
    @Json(name = "code")
    val code: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean
)

