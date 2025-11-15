package com.kau.ttokttok._core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    private val defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun LocalDateTime.formatDate(): String {
        return this.format(defaultFormatter)
    }

    fun LocalDateTime.formatTime(): String {
        return this.format(timeFormatter)
    }

    fun LocalDateTime.formatDateTime(): String {
        return this.format(dateTimeFormatter)
    }
}