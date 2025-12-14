package com.kau.ttokttok.ui.component.common.textfield

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun PostContentField(
    content: String,
    onValueChange: (String) -> Unit
) {
    LabeledOutlinedTextField(
        label = "내용",
        value = content,
        onValueChange = onValueChange,
        placeholder = "내용을 입력하세요",
        singleLine = false,
        minHeight = 120.dp
    )
}