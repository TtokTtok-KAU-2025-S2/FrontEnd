package com.kau.ttokttok.data.remote.dto.noiseboard.res

import java.time.LocalDateTime

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
    val summary: String
)
