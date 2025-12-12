package com.kau.ttokttok.ui.compose.community.writing

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.theme.*

@Preview
@Composable
fun WritingCommunityScreen(
    modifier: Modifier = Modifier,
    onClickCreate: (String, String, Uri?) -> Unit = { _, _, _ -> },
    onClickBack: () -> Unit = { },
) {

    var title by remember { mutableStateOf("")}
    var content by remember { mutableStateOf("")}
    
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    
    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            selectedImageUri = uri
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 헤더
        WhiteHeader(
            onBack = onClickBack
        )

        Spacer(Modifier.height(32.dp))

        // 제목 입력
        PostTitleField(
            title = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(32.dp))

        PostContentField(
            content = content,
            onValueChange = { content = it }
        )

        Spacer(Modifier.height(32.dp))

        PostImagePickerButton(
            selectedImageUri = selectedImageUri,
            onClickAddImage = {
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )

        Spacer(Modifier.height(32.dp))

        PostBottomActions(
            enabled = title.isNotBlank() && content.isNotBlank(),
            onSubmit = {
                onClickCreate(title, content, selectedImageUri)
            }
        )
    }
}

@Composable
fun PostTitleField(
    title: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Label
        Text(
            text = "제목",
            color = Gray700,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Input
        OutlinedTextField(
            value = title,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(White),
            placeholder = { Text("제목을 입력하세요", color = Gray700.copy(alpha = 0.5f)) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Gray700,
                unfocusedIndicatorColor = Gray200
            )
        )
    }
}

@Composable
fun PostContentField(
    content: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // 좌우 여백
    ) {
        // Label
        Text(
            text = "내용",
            color = Gray700,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Multiline Input
        OutlinedTextField(
            value = content,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .heightIn(min = 120.dp),
            placeholder = {
                Text(
                    text = "내용을 입력하세요",
                    color = Gray700.copy(alpha = 0.5f)
                )
            },
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Gray700,
                unfocusedIndicatorColor = Gray200
            )
        )
    }
}

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

@Composable
fun PostBottomActions(
    enabled: Boolean,
    onSubmit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .border(BorderStroke(0.5.dp, Gray200))
            .padding(16.dp), // p-4
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onSubmit,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // w-full height 감각적으로 맞춤
            colors = ButtonDefaults.buttonColors(
                containerColor = if (enabled) Blue600 else Blue600.copy(alpha = 0.4f),
                contentColor = White,
                disabledContainerColor = Blue600.copy(alpha = 0.3f),
                disabledContentColor = Color.White.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "게시하기",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}