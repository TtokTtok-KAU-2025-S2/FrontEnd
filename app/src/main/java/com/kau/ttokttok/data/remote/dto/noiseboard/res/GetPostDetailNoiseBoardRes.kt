package com.kau.ttokttok.data.remote.dto.noiseboard.res

import java.time.LocalDateTime

data class GetPostDetailNoiseBoardRes(
    val reportId: Long,
    val authorDong: Int,
    val reportedAt: String,
    val category: String,
    val summary: String,
    val voteCounts: VoteCount,
    val comments: List<Comment>
)

// TODO: 추후 Enum으로 변경하기
data class VoteCount(
    val HEARD: Int,
    val NOT_HEARD: Int,
    val BE_CAREFUL: Int
)

data class Comment(
    val commentId: Long,
    val authorDong: Int,
    val content: String,
    val createdAt: String,
    val isMyComment: Boolean
)
