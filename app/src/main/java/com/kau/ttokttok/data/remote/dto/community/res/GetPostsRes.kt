package com.kau.ttokttok.data.remote.dto.community.res

import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GetPostsRes(
    val notices: List<Notice>
)

data class Notice(
    val noticeId: Long,
    val title: String,
    val createdAt: String
)

fun Notice.toCommunityBoard() = CommunityBoard(
    id = noticeId,
    createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME),
    title = title
)
