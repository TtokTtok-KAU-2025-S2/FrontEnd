package com.kau.ttokttok.ui.component.common.textfield

import androidx.compose.runtime.Composable

@Composable
fun PostTitleField(
    title: String,
    onValueChange: (String) -> Unit
) {
    LabeledOutlinedTextField(
        label = "제목",
        value = title,
        onValueChange = onValueChange,
        placeholder = "제목을 입력하세요",
        singleLine = true
    )
}