package com.kau.ttokttok.domain.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.remote.dto.preconsideration.req.CreatePostPreConsiderationReq
import com.kau.ttokttok.data.remote.dto.preconsideration.req.ModifyPostPreConsiderationReq
import com.kau.ttokttok.data.remote.dto.preconsideration.res.CreatePostPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.DeletePostPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.GetPostDetailPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.GetPostsPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.ModifyPostPreConsiderationRes

interface PreConsiderationRepository {
    suspend fun getPosts(): NetworkResult<GetPostsPreConsiderationRes>
    suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailPreConsiderationRes>
    suspend fun createPost(req: CreatePostPreConsiderationReq): NetworkResult<CreatePostPreConsiderationRes>
    suspend fun modifyPost(
        id: Long,
        req: ModifyPostPreConsiderationReq
    ): NetworkResult<ModifyPostPreConsiderationRes>
    suspend fun deletePost(id: Long): NetworkResult<DeletePostPreConsiderationRes>
}