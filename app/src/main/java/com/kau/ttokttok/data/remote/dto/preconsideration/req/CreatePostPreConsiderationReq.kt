package com.kau.ttokttok.data.remote.dto.preconsideration.req

data class CreatePostPreConsiderationReq(
    val title: String,
    val content: String,
    val eventDate: String,
    val eventTime: String,
    val eventReason: String
)