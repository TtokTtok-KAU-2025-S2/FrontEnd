package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.preconsideration.req.*
import com.kau.ttokttok.data.remote.dto.preconsideration.res.*
import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail

interface PreConsiderationRepository {
    suspend fun getPosts(): NetworkResult<GetPostsPreConsiderationRes>
    suspend fun getPostDetail(id: Long): Result<PreConsiderationBoardDetail>
    suspend fun createPost(req: CreatePostPreConsiderationReq): NetworkResult<CreatePostPreConsiderationRes>
    suspend fun modifyPost(
        id: Long,
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): ModifyPostPreConsiderationRes
    suspend fun deletePost(id: Long): Result<String>
}