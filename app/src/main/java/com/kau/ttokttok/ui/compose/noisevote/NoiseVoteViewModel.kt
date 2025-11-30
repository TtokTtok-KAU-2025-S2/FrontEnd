package com.kau.ttokttok.ui.compose.noisevote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.data.local.repository.NoiseVoteRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoiseVoteViewModel @Inject constructor(
    private val repository: NoiseVoteRepositoryImpl
): ViewModel() {
    private val _posts = MutableStateFlow<List<NoiseReport>>(emptyList())
    val posts: StateFlow<List<NoiseReport>> = _posts

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            Log.d("NoiseVoteViewModel", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d("NoiseVoteViewModel", "소음현황판 데이터 로딩 시작")
            Log.d("NoiseVoteViewModel", "요청 URL: GET /api/noise-reports")

            when (val result = repository.getPosts()) {
                is NetworkResult.Success -> {
                    Log.d("NoiseVoteViewModel", "✅ 소음현황판 데이터 로딩 성공!")
                    Log.d("NoiseVoteViewModel", "  - 전체 게시글 수: ${result.data.reports.size}개")
                    Log.d("NoiseVoteViewModel", "  - 페이지 정보: ${result.data.listSize}개 (${result.data.totalElements}개 중)")

                    val uiPosts = result.data.reports.map { dto ->
                        Log.d("NoiseVoteViewModel", "  📋 게시글 ID: ${dto.reportId}")
                        Log.d("NoiseVoteViewModel", "    - 작성자: ${dto.authorDong}동")
                        Log.d("NoiseVoteViewModel", "    - Summary: ${dto.summary}")
                        Log.d("NoiseVoteViewModel", "    - 작성일: ${dto.reportedAt}")
                        Log.d("NoiseVoteViewModel", "    - 카테고리: ${dto.category}")

                        NoiseReport(
                            id = dto.reportId,
                            authorLocation = String.format("%s동", dto.authorDong),
                            summary = dto.summary
                        )
                    }

                    _posts.value = uiPosts
                    Log.d("NoiseVoteViewModel", "소음현황판 UI 업데이트 완료 (${uiPosts.size}개)")
                }

                is NetworkResult.Error -> {
                    Log.e("NoiseVoteViewModel", "❌ 소음현황판 데이터 로딩 실패!")
                    Log.e("NoiseVoteViewModel", "  - HTTP 코드: ${result.code}")
                    Log.e("NoiseVoteViewModel", "  - 에러 메시지: ${result.message}")
                    result.exception?.let {
                        Log.e("NoiseVoteViewModel", "  - Exception: ${it.message}", it)
                    }
                    _posts.value = emptyList()
                }
            }

            Log.d("NoiseVoteViewModel", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        }
    }
}