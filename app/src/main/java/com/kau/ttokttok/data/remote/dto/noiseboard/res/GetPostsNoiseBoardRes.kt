package com.kau.ttokttok.data.remote.dto.noiseboard.res

import java.time.LocalDateTime

data class GetPostsNoiseBoardRes(
    val reports: List<NoiseBoard>,

    val listSize: Int,
    val totalPage: Int,
    val totalElement: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

data class NoiseBoard(
    val reportId: Long,
    val authorDong: Int,
    val reportedAt: LocalDateTime,
    // TODO: 추후 Enum으로 변경하기
    val category: String,
    val summary: String
)
