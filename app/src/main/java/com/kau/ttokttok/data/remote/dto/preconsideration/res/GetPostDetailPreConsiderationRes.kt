package com.kau.ttokttok.data.remote.dto.preconsideration.res

import java.time.LocalDateTime

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
