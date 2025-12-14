package com.kau.ttokttok.ui.compose.preconsideration

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
import com.kau.ttokttok.ui.component.common.header.BoardHeader
import com.kau.ttokttok.ui.component.common.board.PreConsiderationBoardCard
import com.kau.ttokttok.ui.theme.Slate900


data class PreConsiderationPost(
    val id: Long,
    val title: String,
    val authorLocation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreConsiderationScreen(
    modifier: Modifier = Modifier,
    uiState: PreConsiderationUiState = PreConsiderationUiState(),
    onClickCreatePost: () -> Unit = { },
    onClickPost: (Long) -> Unit = { },
    onRefresh: () -> Unit = {}
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(bottom = 64.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // 헤더
        BoardHeader(
            onClickCreatePost = onClickCreatePost,
            title = "사전 양해 게시판",
            titleDescription = "이웃들에게 미리 알려주세요"
        )

        Spacer(Modifier.height(16.dp))

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
                    items = uiState.posts ?: emptyList(),
                    key = { it.id }
                ) { post ->
                    PreConsiderationBoardCard(
                        title = post.title,
                        authorLocation = post.authorLocation,
                        onClick = { onClickPost(post.id) }
                    )
                }
            }
        }
    }
}
