package com.kau.ttokttok.data.remote.dto.noiseboard.res

import com.kau.ttokttok.domain.model.board.Comment
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoardDetail
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GetPostDetailNoiseBoardRes(
    val reportId: Long,
    val authorDong: Int,
    val reportedAt: String,
    val category: String,
    val summary: String?,

    val maxDb: Double,
    val avgDb: Double,

    val voteCounts: Map<String, Int>?,

    val comments: List<CommentDTO>?
)

fun GetPostDetailNoiseBoardRes.toNoiseVoteBoardDetail(): NoiseVoteBoardDetail {
    return NoiseVoteBoardDetail(
        buildingNumber = authorDong,
        reportedAt = LocalDateTime.parse(reportedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        category = category,
        title = summary ?: "",

        maxDb = maxDb.toInt(),
        avgDb = avgDb.toInt(),

        voteCount = voteCounts?.mapKeys { (key, _ ) -> NoiseVoteType.from(key) } ?: emptyMap(),
        comments = comments?.map { dto ->
            dto.toDomain()
        } ?: emptyList()
    )
}

data class CommentDTO(
    val commentId: Long,
    val authorDong: Int,
    val content: String,
    val createdAt: String,
    val isMyComment: Boolean
)

fun CommentDTO.toDomain(): Comment {
    return Comment(
        id = commentId,
        buildingNumber = authorDong,
        content = content,
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        isMyComment = isMyComment
    )
}
