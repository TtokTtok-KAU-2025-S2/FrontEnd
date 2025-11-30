package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.NoiseStatusBoardApiService
import com.kau.ttokttok.data.remote.dto.noiseboard.req.*
import com.kau.ttokttok.data.remote.dto.noiseboard.res.*
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoard
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteBoardDetail
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoiseVoteRepositoryImpl @Inject constructor(
    private val api: NoiseStatusBoardApiService
): NoiseVoteRepository {
    override suspend fun getPosts(): Result<List<NoiseVoteBoard>> {
        return when (val response = safeApiCall { api.getPosts() }) {
            is NetworkResult.Success -> {
                Result.success(response.data.reports.map{
                    it.toNoiseVoteBoard()
                })
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message ?: "게시글 불러오기에 실패했습니다."))
            }
        }
    }

    override suspend fun getPostDetail(id: Long): Result<NoiseVoteBoardDetail> {
        return when (val response = safeApiCall { api.getPostDetail(id) }) {
            is NetworkResult.Success -> {
                Result.success(response.data.toNoiseVoteBoardDetail())
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }

    override suspend fun firstVote(
        id: Long,
        voteType: NoiseVoteType
    ): Result<FirstVoteRes> {
        val req = FirstVoteReq(
            voteType = voteType
        )

        return when (val response = safeApiCall { api.firstVote(id, req) }) {
            is NetworkResult.Success -> {
                Result.success(response.data)
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }


    override suspend fun postComment(
        id: Long,
        content: String
    ): Result<PostCommentRes> {
        val req = PostCommentReq(
            content = content
        )

        return when (val response = safeApiCall {api.postComment(id, req)}) {
            is NetworkResult.Success -> {
                Result.success(response.data)
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }

    override suspend fun modifyComment(
        id: Long,
        content: String
    ): Result<ModifyCommentRes> {
        val req = ModifyCommentReq(
            content = content
        )

        return when (val response = safeApiCall {api.modifyComment(id, req)}) {
            is NetworkResult.Success -> {
                Result.success(response.data)
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }

    override suspend fun cancelVote(id: Long): Result<String> {
        return when (val response = safeApiCall { api.cancelVote(id) }) {
            is NetworkResult.Success -> {
                Result.success(response.data)
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }

    override suspend fun deleteComment(id: Long): Result<DeleteCommentRes> {
        return when (val response = safeApiCall { api.deleteComment(id) }) {
            is NetworkResult.Success -> {
                Result.success(response.data)
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }
}