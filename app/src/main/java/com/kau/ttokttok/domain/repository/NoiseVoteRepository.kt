package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.board.noisevote.*
import com.kau.ttokttok.domain.usecase.noisevote.PostVoteResult

interface NoiseVoteRepository {
    suspend fun getPosts(): Result<List<NoiseVoteBoard>>
    suspend fun getPostDetail(id: Long): Result<NoiseVoteBoardDetail>
    suspend fun firstVote(
        id: Long,
        voteType: NoiseVoteType
    ): Result<PostVoteResult>

    suspend fun postComment(
        id: Long,
        content: String
    ): Result<Unit>

    suspend fun modifyComment(
        id: Long,
        content: String
    ): Result<Unit>

    suspend fun cancelVote(
        id: Long
    ): Result<Unit>

    suspend fun deleteComment(
        id: Long
    ): Result<Unit>
}