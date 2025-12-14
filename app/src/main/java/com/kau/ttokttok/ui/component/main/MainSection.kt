package com.kau.ttokttok.ui.component.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.R
import com.kau.ttokttok.ui.compose.main.MainUiState
import com.kau.ttokttok.ui.navigation.Destination

@Composable
fun MainSection(
    modifier: Modifier = Modifier,
    uiState: MainUiState = MainUiState(),
    onButtonClick: (Destination) -> Unit = {}
) {
    var currentSlide by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp) // px-6
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)) // rounded-3xl
                    .background(Color.White.copy(alpha = 0.10f)) // bg-white/10
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp) // h-48
                            .background(Color.Black)
                    ) {
                        Image(
                            modifier = modifier
                                .fillMaxSize(),
                            painter = painterResource(id = uiState.mainSlides[currentSlide].imageRes),
                            contentDescription = uiState.mainSlides[currentSlide].title,
                            contentScale = ContentScale.Crop
                        )

                        // Prev 버튼
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterStart)
                                .offset(x = 8.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .clickable {
                                    currentSlide =
                                        if (currentSlide == 0) uiState.mainSlides.size - 1 else currentSlide - 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_left),
                                contentDescription = "Prev",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Next 버튼
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterEnd)
                                .offset(x = (-8).dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .clickable {
                                    currentSlide =
                                        if (currentSlide == uiState.mainSlides.size - 1) 0 else currentSlide + 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 텍스트 + 버튼
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.mainSlides[currentSlide].title,
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = uiState.mainSlides[currentSlide].description,
                            color = Color(0xFFE9D5FF),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Button(
                            onClick = { onButtonClick(uiState.mainSlides[currentSlide].destination) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_volume),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = uiState.mainSlides[currentSlide].buttonText
                            )
                        }
                    }

                    // 인디케이터
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        uiState.mainSlides.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width(if (index == currentSlide) 24.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == currentSlide)
                                            Color.White
                                        else
                                            Color.White.copy(alpha = 0.4f)
                                    )
                                    .padding(horizontal = 2.dp)
                                    .clickable { currentSlide = index }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }
}