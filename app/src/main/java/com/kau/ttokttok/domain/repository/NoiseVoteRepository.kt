package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.data.remote.dto.noiseboard.req.*
import com.kau.ttokttok.data.remote.dto.noiseboard.res.*
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoardDetail
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType

interface NoiseVoteRepository {
    suspend fun getPosts(): Result<List<NoiseVoteBoard>>
    suspend fun getPostDetail(id: Long): Result<NoiseVoteBoardDetail>
    suspend fun firstVote(
        id: Long,
        voteType: NoiseVoteType
    ): Result<FirstVoteRes>

    suspend fun postComment(
        id: Long,
        content: String
    ): Result<PostCommentRes>

    suspend fun modifyComment(
        id: Long,
        content: String
    ): Result<ModifyCommentRes>

    suspend fun cancelVote(
        id: Long
    ): Result<CancelVoteRes>

    suspend fun deleteComment(
        id: Long
    ): Result<DeleteCommentRes>
}