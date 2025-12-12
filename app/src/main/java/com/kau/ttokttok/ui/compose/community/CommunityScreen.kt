package com.kau.ttokttok.ui.compose.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.board.CommunityBoardCard
import com.kau.ttokttok.ui.component.common.header.BoardHeader
import com.kau.ttokttok.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    uiState: CommunityUiState = CommunityUiState(),
    onClickCreatePost: () -> Unit = { },
    onClickPost: (Long) -> Unit = { },
    onRefresh: () -> Unit = {}
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
            .statusBarsPadding()
            .padding(bottom = 64.dp)       // pb-16
    ) {
        // Header
        BoardHeader(
            onClickCreatePost = onClickCreatePost,
            title = "공지사항",
            titleDescription = "이웃과 소통하고 정보를 공유해보세요"
        )

        Spacer(Modifier.height(32.dp))

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(
                    items = uiState.posts,
                    key = { post -> post.id }
                ) { post ->
                    CommunityBoardCard(
                        title = post.title,
                        createdAt = post.createdAt,
                        onClick = { onClickPost(post.id) }
                    )
                }
            }
        }
    }
}