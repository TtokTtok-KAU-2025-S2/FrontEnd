package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.PreNoticeApiService
import com.kau.ttokttok.data.remote.dto.preconsideration.req.CreatePostPreConsiderationReq
import com.kau.ttokttok.data.remote.dto.preconsideration.req.ModifyPostPreConsiderationReq
import com.kau.ttokttok.data.remote.dto.preconsideration.res.CreatePostPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.DeletePostPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.GetPostDetailPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.GetPostsPreConsiderationRes
import com.kau.ttokttok.data.remote.dto.preconsideration.res.ModifyPostPreConsiderationRes
import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreConsiderationRepositoryImpl @Inject constructor(
    private val api: PreNoticeApiService
): PreConsiderationRepository {
    override suspend fun getPosts(): NetworkResult<GetPostsPreConsiderationRes> =
        safeApiCall { api.getPosts() }

    override suspend fun getPostDetail(id: Long): NetworkResult<GetPostDetailPreConsiderationRes> =
        safeApiCall { api.getPostDetail(id) }

    override suspend fun createPost(req: CreatePostPreConsiderationReq): NetworkResult<CreatePostPreConsiderationRes> =
        safeApiCall { api.createPost(req) }

    override suspend fun modifyPost(
        id: Long,
        req: ModifyPostPreConsiderationReq
    ): NetworkResult<ModifyPostPreConsiderationRes> =
        safeApiCall { api.modifyPost(id, req) }

    override suspend fun deletePost(id: Long): NetworkResult<DeletePostPreConsiderationRes> =
        safeApiCall { api.deletePost(id) }
}