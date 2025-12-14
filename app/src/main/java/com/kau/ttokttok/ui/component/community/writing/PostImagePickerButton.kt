package com.kau.ttokttok.ui.component.community.writing

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kau.ttokttok.ui.theme.*

@Composable
fun PostImagePickerButton(
    modifier: Modifier = Modifier,
    selectedImageUri: Uri? = null,
    onClickAddImage: () -> Unit = {},
    onClickRemoveImage: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(100.dp)
            .background(White, RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Gray200, shape = RoundedCornerShape(12.dp))
            .clickable { onClickAddImage() },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "사진 추가",
                    tint = Blue600,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "사진 추가",
                    color = Gray400,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        else {
            AsyncImage(
                model = selectedImageUri, // Uri 그대로 전달
                contentDescription = "선택된 이미지",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        RoundedCornerShape(999.dp)
                    )
                    .clickable { onClickRemoveImage() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "삭제",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}