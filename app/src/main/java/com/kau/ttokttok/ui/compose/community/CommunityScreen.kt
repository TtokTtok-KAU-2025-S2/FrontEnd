package com.kau.ttokttok.ui.compose.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.board.CommunityBoardCard
import com.kau.ttokttok.ui.component.common.header.BoardHeader

private val Slate900 = Color(0xFF0F172A) // bg-slate-900
private val Blue700  = Color(0xFF1D4ED8) // hover:bg-blue-700

data class CommunityPost(
    val id: Long,
    val title: String,
    val authorLocation: String,
)

@Preview
@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    onClickCreatePost: () -> Unit = { },
    onClickPost: (Long) -> Unit = { }
) {
    val samplePosts = listOf(
        CommunityPost(
            id = 1,
            title = "층간소음 관련 안내드립니다",
            authorLocation = "302동"
        ),
        CommunityPost(
            id = 2,
            title = "주말 엘리베이터 점검 공지",
            authorLocation = "101동",
        ),
        CommunityPost(
            id = 3,
            title = "쓰레기 배출 시간 꼭 지켜주세요",
            authorLocation = "303동",
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)          // flex-col h-full bg-slate-900
            .padding(bottom = 64.dp)       // pb-16
    ) {
        // Header
        BoardHeader(
            onClickCreatePost = onClickCreatePost,
            title = "공지사항",
            titleDescription = "이웃과 소통하고 정보를 공유해보세요"
        )

        Spacer(Modifier.height(32.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(
                items = samplePosts,
                key = { post -> post.id }
            ) { post ->
                CommunityBoardCard(
                    id = post.id,
                    title = post.title,
                    authorLocation = post.authorLocation,
                    onClick = onClickPost
                )
            }
        }
    }
}