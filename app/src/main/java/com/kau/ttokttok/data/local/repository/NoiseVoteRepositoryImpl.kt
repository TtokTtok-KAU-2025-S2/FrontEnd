package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.NoiseStatusBoardApiService
import com.kau.ttokttok.data.remote.dto.noiseboard.req.FirstVoteReq
import com.kau.ttokttok.data.remote.dto.noiseboard.req.ModifyCommentReq
import com.kau.ttokttok.data.remote.dto.noiseboard.req.PostCommentReq
import com.kau.ttokttok.data.remote.dto.noiseboard.res.CancelVoteRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.DeleteCommentRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.FirstVoteRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.GetPostDetailNoiseBoardRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.GetPostsNoiseBoardRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.ModifyCommentRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.PostCommentRes
import com.kau.ttokttok.data.remote.dto.noiseboard.res.toNoiseVoteBoard
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoiseVoteRepositoryImpl @Inject constructor(
    private val api: NoiseStatusBoardApiService
): NoiseVoteRepository {
    override suspend fun getPosts(): List<NoiseVoteBoard> {
        return when (val response = safeApiCall { api.getPosts() }) {
            is NetworkResult.Success -> {
                response.data.reports.map {
                    it.toNoiseVoteBoard()
                }
            }

            is NetworkResult.Error -> {
                throw Exception(response.message ?: "게시글 불러오기에 실패했습니다.")
            }
        }
    }

    override suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailNoiseBoardRes> =
        safeApiCall { api.getPostDetail(id) }

    override suspend fun firstVote(
        id: Long,
        req: FirstVoteReq
    ): NetworkResult<FirstVoteRes> =
        safeApiCall { api.firstVote(id, req) }

    override suspend fun postComment(
        id: Long,
        req: PostCommentReq
    ): NetworkResult<PostCommentRes> =
        safeApiCall { api.postComment(id, req) }

    override suspend fun modifyComment(
        id: Long,
        req: ModifyCommentReq
    ): NetworkResult<ModifyCommentRes> =
        safeApiCall { api.modifyComment(id, req) }

    override suspend fun cancelVote(id: Long): NetworkResult<CancelVoteRes> =
        safeApiCall { api.cancelVote(id) }

    override suspend fun deleteComment(id: Long): NetworkResult<DeleteCommentRes> =
        safeApiCall { api.deleteComment(id) }
}