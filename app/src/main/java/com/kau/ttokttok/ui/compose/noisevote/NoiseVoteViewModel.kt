package com.kau.ttokttok.ui.compose.noisevote

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
            when (val result = repository.getPosts()) {
                is NetworkResult.Success -> {
                    val uiPosts = result.data.reports.map { dto ->
                        NoiseReport(
                            id = dto.reportId,
                            authorLocation = String.format("%s동", dto.authorDong),
                            summary = dto.summary
                        )
                    }

                    _posts.value = uiPosts
                }

                is NetworkResult.Error -> {
                    _posts.value = emptyList()
                }
            }
        }
    }
}