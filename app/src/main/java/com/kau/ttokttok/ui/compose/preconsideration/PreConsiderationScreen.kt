package com.kau.ttokttok.ui.compose.preconsideration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.header.BoardHeader
import com.kau.ttokttok.ui.component.common.board.PreConsiderationBoardCard

private val Slate900 = Color(0xFF0F172A) // bg-slate-900

data class PreConsiderationPost(
    val id: Long,
    val title: String,
    val authorLocation: String
)

@Preview(showBackground = true)
@Composable
fun PreConsiderationScreen(
    modifier: Modifier = Modifier,
    onClickCreatePost: () -> Unit = { },
    onClickPost: (Long) -> Unit = { }
) {
    // ✅ 예시 데이터 2~3개
    val samplePosts = listOf(
        PreConsiderationPost(
            id = 1L,
            title = "11/20(수) 오전 9~12시 가구 이동 예정",
            authorLocation = "302동"
        ),
        PreConsiderationPost(
            id = 2L,
            title = "욕실 공사 안내 (타일 시공, 소음 예상)",
            authorLocation = "101동"
        ),
        PreConsiderationPost(
            id = 3L,
            title = "아이 생일파티로 점심 시간대 소음 양해 부탁...",
            authorLocation = "303동"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(bottom = 64.dp)
    ) {
        // 헤더
        BoardHeader(
            onClickCreatePost = onClickCreatePost,
            title = "사전 양해 게시판",
            titleDescription = "이웃들에게 미리 알려주세요"
        )

        Spacer(Modifier.height(16.dp))

        // ✅ 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(
                items = samplePosts,
                key = { it.id }
            ) { post ->
                PreConsiderationBoardCard(
                    id = post.id,
                    title = post.title,
                    authorLocation = post.authorLocation,
                    onClick = onClickPost
                )
            }
        }
    }
}
