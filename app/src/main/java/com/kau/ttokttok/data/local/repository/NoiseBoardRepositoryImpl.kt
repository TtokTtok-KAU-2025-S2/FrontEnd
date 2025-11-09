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
import com.kau.ttokttok.domain.repository.NoiseBoardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoiseBoardRepositoryImpl @Inject constructor(
    private val api: NoiseStatusBoardApiService
): NoiseBoardRepository {
    override suspend fun getPosts(): NetworkResult<GetPostsNoiseBoardRes> =
        safeApiCall { api.getPosts() }

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