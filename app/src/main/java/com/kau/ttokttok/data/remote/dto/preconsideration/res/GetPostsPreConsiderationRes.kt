package com.kau.ttokttok.data.remote.dto.preconsideration.res

data class GetPostsPreConsiderationRes(
    val preNotices: List<PreNotice>
)

data class PreNotice(
    val preNoticeId: Long,
    val authorDong: Int,
    val authorHosu: Int,
    val title: String,
    val eventDate: String,
    val eventTime: String,
    val eventReason: String,
    val createdAt: String,
)
