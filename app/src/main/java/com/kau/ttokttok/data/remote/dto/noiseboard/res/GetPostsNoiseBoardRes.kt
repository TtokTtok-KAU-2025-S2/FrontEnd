package com.kau.ttokttok.data.remote.dto.noiseboard.res

import android.health.connect.LocalTimeRangeFilter
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GetPostsNoiseBoardRes(
    val reports: List<NoiseBoard>,

    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

data class NoiseBoard(
    val reportId: Long,
    val authorDong: Int,
    val reportedAt: String,
    val category: String,
    val summary: String?
)

fun NoiseBoard.toNoiseVoteBoard(): NoiseVoteBoard {
    return NoiseVoteBoard(
        id = reportId,
        authorLocation = String.format("%s동", authorDong),
        title = summary ?: "",
        reportedAt = LocalDateTime.parse(reportedAt, DateTimeFormatter.ISO_DATE_TIME),
        category = category
    )
}
