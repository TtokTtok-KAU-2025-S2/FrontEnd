package com.kau.ttokttok.data.remote.dto.community.res

import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GetPostDetailRes(
    val noticeId: Long,
    val title: String,
    val content: String,
    val createdAt: String
)

fun GetPostDetailRes.toCommunityBoardDetail() : CommunityBoardDetail {
    return CommunityBoardDetail(
        title = title,
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME),
        content = content
    )
}