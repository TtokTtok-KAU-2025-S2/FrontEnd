package com.kau.ttokttok.domain.model.board

import java.time.LocalDateTime

sealed interface BoardPost {
    val buildingNumber: String
    val localDateTime: LocalDateTime
    val title: String
    val content: String
    val commentValue: Int
}