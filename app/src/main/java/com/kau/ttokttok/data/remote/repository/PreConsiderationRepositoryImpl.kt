package com.kau.ttokttok.data.remote.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.PreNoticeApiService
import com.kau.ttokttok.data.remote.dto.preconsideration.req.*
import com.kau.ttokttok.data.remote.dto.preconsideration.res.*
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreConsiderationRepositoryImpl @Inject constructor(
    private val api: PreNoticeApiService
): PreConsiderationRepository {
    override suspend fun getPosts(): NetworkResult<GetPostsPreConsiderationRes> =
        safeApiCall { api.getPosts() }

    override suspend fun getPostDetail(id: Long): Result<PreConsiderationBoardDetail> {
        return when (val response = safeApiCall { api.getPostDetail(id) }) {
            is NetworkResult.Success -> {
                Result.success(response.data.toPreConsiderationBoardDetail())
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }

    override suspend fun createPost(req: CreatePostPreConsiderationReq): NetworkResult<CreatePostPreConsiderationRes> =
        safeApiCall { api.createPost(req) }

    override suspend fun modifyPost(
        id: Long,
        req: ModifyPostPreConsiderationReq
    ): NetworkResult<ModifyPostPreConsiderationRes> =
        safeApiCall { api.modifyPost(id, req) }

    override suspend fun deletePost(id: Long): Result<String> {
        return when (val response = safeApiCall { api.deletePost(id) } ) {
            is NetworkResult.Success -> {
                Result.success(response.data.result)
            }

            is NetworkResult.Error -> {
                Result.failure(Exception(response.message))
            }
        }
    }
}