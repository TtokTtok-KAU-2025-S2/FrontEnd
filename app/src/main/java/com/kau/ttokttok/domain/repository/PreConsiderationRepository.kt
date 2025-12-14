package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.board.preconsideration.PreConsiderationBoardDetail
import com.kau.ttokttok.ui.compose.preconsideration.PreConsiderationPost

interface PreConsiderationRepository {
    suspend fun getPosts(): Result<List<PreConsiderationPost>>
    suspend fun getPostDetail(id: Long): Result<PreConsiderationBoardDetail>
    suspend fun createPost(
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): Result<Unit>

    suspend fun modifyPost(
        id: Long,
        title: String,
        content: String,
        noticeDate: String,
        noticeTime: String,
        noticeReason: String
    ): Result<Unit>
    suspend fun deletePost(id: Long): Result<Unit>
}