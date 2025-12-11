package com.kau.ttokttok.ui.component.preconsideration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok._core.util.DateUtils.formatDateTime
import com.kau.ttokttok.ui.compose.community.detail.Gray200
import com.kau.ttokttok.ui.compose.community.detail.Gray400
import com.kau.ttokttok.ui.compose.community.detail.Gray500
import com.kau.ttokttok.ui.compose.preconsideration.detail.PreConsiderationDetailUiState
import com.kau.ttokttok.ui.theme.Gray50Bg
import com.kau.ttokttok.ui.theme.Gray900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreConsiderationBoardDetailContent(
    modifier: Modifier = Modifier,
    uiState: PreConsiderationDetailUiState = PreConsiderationDetailUiState(),
    onRefresh: () -> Unit = { }
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier
            .background(Gray50Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        state = pullToRefreshState,
        isRefreshing = uiState.isLoading,
        onRefresh = onRefresh
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
                        text = String.format("%s", uiState.preConsiderationBoardDetail?.buildingNumber),
                        color = Gray500,
                        fontSize = 12.sp
                    )
                    Text(
                        text = uiState.preConsiderationBoardDetail?.createdAt?.formatDateTime() ?: "",
                        color = Gray400,
                        fontSize = 11.sp
                    )
                }

                // 제목
                Text(
                    text = uiState.preConsiderationBoardDetail?.title ?: "",
                    color = Gray900,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = uiState.preConsiderationBoardDetail?.content ?: "",
                    color = Gray900,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}