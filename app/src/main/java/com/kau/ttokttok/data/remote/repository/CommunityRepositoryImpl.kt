package com.kau.ttokttok.data.remote.repository

import android.content.Context
import com.kau.ttokttok._core.network.auth.toPlainRequestBody
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok._core.network.result.safeApiCall
import com.kau.ttokttok.data.remote.api.CommunityApiService
import com.kau.ttokttok.data.remote.dto.community.res.*
import com.kau.ttokttok.domain.model.board.community.CommunityBoard
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.domain.repository.CommunityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val api: CommunityApiService,
    @ApplicationContext private val context: Context
) : CommunityRepository {
    override suspend fun createPost(title: String, content: String, imageUri: String?): String {
        val noticePicturePart = imageUri?.let { uriString ->
            uriStringToImagePart(uriString, partName = "noticePicture")
        }

        return when (val response = safeApiCall { api.createPost(
            title = title.toPlainRequestBody(),
            content = content.toPlainRequestBody(),
            noticePicture = noticePicturePart
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

    private fun uriStringToImagePart(
        uriString: String,
        partName: String // 서버 필드명
    ): MultipartBody.Part? {
        val uri = uriString.toUri()
        val resolver = context.contentResolver

        val mimeType = resolver.getType(uri) ?: "image/*"
        val bytes = resolver.openInputStream(uri)?.use { it.readBytes() } ?: return null

        val fileName = "upload_${System.currentTimeMillis()}.jpg"
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            partName,
            fileName,
            requestBody
        )
    }
}