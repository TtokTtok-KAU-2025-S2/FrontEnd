package com.kau.ttokttok.domain.model.board

import java.sql.Time
import java.time.LocalDateTime
import java.util.Date

data class PreApologyBoard(
    override val buildingNumber: String,
    override val localDateTime: LocalDateTime,
    override val title: String,
    override val content: String,
    override val commentValue: Int,

    val date: Date,
    val startTime: Time,
    val endTime: Time,
    val reason: String
) : BoardPost