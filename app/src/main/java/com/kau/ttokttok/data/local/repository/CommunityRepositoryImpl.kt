package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.CommunityApiService
import com.kau.ttokttok.data.remote.dto.community.req.*
import com.kau.ttokttok.data.remote.dto.community.res.*
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.repository.CommunityRepository
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val api: CommunityApiService
) : CommunityRepository {
    override suspend fun createPost(req: CreatePostCommunityReq): NetworkResult<CreatePostCommunityRes> =
        safeApiCall { api.createPost(req) }

    override suspend fun getPosts(): Result<List<CommunityBoard>> {
        return when (val response = safeApiCall { api.getPosts() }) {
            is NetworkResult.Success -> {
                // TODO: 백엔드 구현 후 연결하기
                Result.success(emptyList())
                // Result.success(response.data.toDomain())
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }

    override suspend fun getPostDetail(id: Long): Result<CommunityBoardDetail> {
        return when (val response = safeApiCall { api.getPostDetail(id) }) {
            is NetworkResult.Success -> {
                // TODO: 백엔드 구현 후 연결하기
                Result.success(CommunityBoardDetail(title = "", content = "", createdAt = LocalDateTime.now(), author = ""))
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }
}