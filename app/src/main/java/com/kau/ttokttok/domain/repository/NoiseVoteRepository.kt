package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.noiseboard.req.FirstVoteReq
import com.kau.ttokttok.data.remote.dto.noiseboard.req.ModifyCommentReq
import com.kau.ttokttok.data.remote.dto.noiseboard.req.PostCommentReq
import com.kau.ttokttok.data.remote.dto.noiseboard.res.CancelVoteRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.DeleteCommentRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.FirstVoteRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.GetPostDetailNoiseBoardRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.ModifyCommentRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.PostCommentRes
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard

interface NoiseVoteRepository {
    suspend fun getPosts(): List<NoiseVoteBoard>
    suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailNoiseBoardRes>
    suspend fun firstVote(
        id: Long,
        req: FirstVoteReq
    ): NetworkResult<FirstVoteRes>

    suspend fun postComment(
        id: Long,
        req: PostCommentReq
    ): NetworkResult<PostCommentRes>

    suspend fun modifyComment(
        id: Long,
        req: ModifyCommentReq
    ): NetworkResult<ModifyCommentRes>

    suspend fun cancelVote(
        id: Long
    ): NetworkResult<CancelVoteRes>

    suspend fun deleteComment(
        id: Long
    ): NetworkResult<DeleteCommentRes>
}