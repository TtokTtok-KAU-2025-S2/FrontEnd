package com.kau.ttokttok.data.remote.repository

import com.kau.ttokttok._core.network.result.*
import com.kau.ttokttok.data.remote.api.PreNoticeApiService
import com.kau.ttokttok.data.remote.dto.preconsideration.req.*
import com.kau.ttokttok.data.remote.dto.preconsideration.res.*
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import com.kau.ttokttok.ui.compose.preconsideration.PreConsiderationPost
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreConsiderationRepositoryImpl @Inject constructor(
    private val api: PreNoticeApiService
): PreConsiderationRepository {
    override suspend fun getPosts(): Result<List<PreConsiderationPost>> {
        return when (val response = safeApiCall { api.getPosts() }) {
            is NetworkResult.Success -> {
                Result.success(response.data.preNotices.map {
                    it.toPreConsiderationPost()
                })
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }

    }

    override suspend fun getPostDetail(id: Long): Result<PreConsiderationBoardDetail> {
        return when (val response = safeApiCall { api.getPostDetail(id) }) {
            is NetworkResult.Success -> {
                Result.success(response.data.toPreConsiderationBoardDetail())
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }

    override suspend fun createPost(
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): Result<Unit> {
        val req = CreatePostPreConsiderationReq(
            title = title,
            content = content,
            eventDate = noticeDate,
            eventTime = noticeTime,
            eventReason = noticeReason
        )

        return when (val response = safeApiCall { api.createPost(req) }) {
            is NetworkResult.Success -> {
                Result.success(Unit)
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }

    override suspend fun modifyPost(
        id: Long,
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): Result<Unit> {
        val req = ModifyPostPreConsiderationReq(
            title = title,
            content = content,
            eventDate = noticeDate,
            eventTime = noticeTime,
            eventReason = noticeReason
        )

        return when (val response = safeApiCall { api.modifyPost(id, req) }) {
            is NetworkResult.Success -> {
                Result.success(Unit)
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }

    }

    override suspend fun deletePost(id: Long): Result<Unit> {
        return when (val response = safeApiCall { api.deletePost(id) } ) {
            is NetworkResult.Success -> {
                Result.success(Unit)
            }

            is NetworkResult.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }
}