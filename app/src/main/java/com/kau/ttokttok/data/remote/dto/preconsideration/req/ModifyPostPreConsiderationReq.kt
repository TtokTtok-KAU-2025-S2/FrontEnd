package com.kau.ttokttok.data.remote.dto.preconsideration.req

data class ModifyPostPreConsiderationReq(
    val title: String,
    val content: String,
    val eventDate: String,
    val eventTime: String,
    val eventReason: String
)
