package com.kau.ttokttok.ui.component.community.writing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kau.ttokttok._core.util.DateUtils.formatDateTime
import com.kau.ttokttok.domain.model.board.community.CommunityBoardDetail
import com.kau.ttokttok.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailContent(
    modifier: Modifier = Modifier,
    communityBoardDetail: CommunityBoardDetail,
    isLoading: Boolean,
    onRefresh: () -> Unit = { }
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier
            .background(Gray50Bg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        state = pullToRefreshState,
        isRefreshing = isLoading,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.5.dp, Gray200),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // 상단 메타: 위치 / 날짜
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = communityBoardDetail.createdAt.formatDateTime(),
                            color = Gray400,
                            fontSize = 11.sp
                        )
                    }

                    // 제목
                    Text(
                        text = communityBoardDetail.title,
                        color = Gray900,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = communityBoardDetail.content,
                        color = Gray900,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val imageUrl = communityBoardDetail.pictureUrl
                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "게시글 이미지",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .heightIn(max = 240.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}