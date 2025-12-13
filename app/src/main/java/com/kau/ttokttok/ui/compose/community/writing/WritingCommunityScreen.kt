package com.kau.ttokttok.ui.compose.community.writing

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kau.ttokttok.ui.component.common.button.PostBottomActions
import com.kau.ttokttok.ui.component.common.header.WhiteHeader
import com.kau.ttokttok.ui.component.common.textfield.PostContentField
import com.kau.ttokttok.ui.component.common.textfield.PostTitleField
import com.kau.ttokttok.ui.component.community.writing.*
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
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // 헤더
        WhiteHeader(
            onBack = onClickBack
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
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
}