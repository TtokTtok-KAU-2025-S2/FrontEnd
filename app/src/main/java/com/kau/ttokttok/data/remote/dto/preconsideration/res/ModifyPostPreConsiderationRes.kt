package com.kau.ttokttok.data.remote.dto.preconsideration.res

import java.time.LocalDateTime

data class ModifyPostPreConsiderationRes(
    val preNoticeId: Long,
    val authorDong: Int,
    val title: String,
    val content: String,
    val eventDate: String,
    val eventTime: String,
    val eventReason: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)
