package com.kau.ttokttok.data.remote.dto.preconsideration.res

import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GetPostDetailPreConsiderationRes(
    val preNoticeId: Long,
    val authorDong: Int,
    val authorHosu: Int,
    val title: String,
    val content: String,
    val eventDate: String,
    val eventTime: String,
    val eventReason: String,
    val createdAt: String,
)

fun GetPostDetailPreConsiderationRes.toPreConsiderationBoardDetail(): PreConsiderationBoardDetail {
    return PreConsiderationBoardDetail(
        title = title,
        content = content,
        buildingNumber = authorDong,
        unitNumber = authorHosu,
        noticeDate = eventDate,
        noticeTime = eventTime,
        noticeReason = eventReason,
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}