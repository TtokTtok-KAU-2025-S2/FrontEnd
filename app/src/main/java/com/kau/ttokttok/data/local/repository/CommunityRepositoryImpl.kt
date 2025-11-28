package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.auth.toPlainRequestBody
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.CommunityApiService
import com.kau.ttokttok.data.remote.dto.community.req.*
import com.kau.ttokttok.data.remote.dto.community.res.*
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val api: CommunityApiService
) : CommunityRepository {
    override suspend fun createPost(title: String, content: String): String {
        return when (val response = safeApiCall { api.createPost(
            title = title.toPlainRequestBody(),
            content = content.toPlainRequestBody(),
            noticePicture = null
        ) }) {
            is NetworkResult.Success -> {
                response.data.title
            }

            is NetworkResult.Error -> {
                throw Throwable(response.message ?: "게시글 작성에 실패했습니다.")
            }
        }
    }

    override suspend fun getPosts(): List<CommunityBoard> {
        return when (val response = safeApiCall { api.getPosts() }) {
            is NetworkResult.Success -> {
                response.data.notices.map {
                    it.toCommunityBoard()
                }
            }

            is NetworkResult.Error -> {
                throw Throwable(response.message ?: "게시글 불러오기에 실패했습니다.")
            }
        }
    }

    override suspend fun getPostDetail(id: Long): CommunityBoardDetail {
        return when (val response = safeApiCall { api.getPostDetail(id) }) {
            is NetworkResult.Success -> {
                response.data.toCommunityBoardDetail()
            }

            is NetworkResult.Error -> {
               throw Throwable(response.message ?: "불러오기에 실패했습니다.")
            }
        }
    }
}