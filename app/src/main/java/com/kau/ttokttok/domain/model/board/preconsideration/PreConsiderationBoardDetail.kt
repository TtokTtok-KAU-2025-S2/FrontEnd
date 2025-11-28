package com.kau.ttokttok.domain.model.board.preconsideration

import java.time.LocalDateTime

data class PreConsiderationBoardDetail(
    val title: String,
    val content: String,
    val buildingNumber: Int,
    val unitNumber: Int,
    val noticeDate: String,
    val noticeTime: String,
    val noticeReason: String,
    val createdAt: LocalDateTime
)
