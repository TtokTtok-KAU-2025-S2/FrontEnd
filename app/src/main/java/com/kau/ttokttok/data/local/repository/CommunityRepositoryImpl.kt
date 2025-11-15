package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.CommunityApiService
import com.kau.ttokttok.data.remote.dto.community.req.*
import com.kau.ttokttok.data.remote.dto.community.res.*
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.repository.CommunityRepository
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

    override suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailRes> =
        safeApiCall { api.getPostDetail(id) }
}