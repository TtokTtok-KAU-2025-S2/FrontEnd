package com.kau.ttokttok.ui.component.common.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.kau.ttokttok.ui.theme.*

@Composable
fun LabeledOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minHeight: Dp = Dp.Unspecified,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = label,
            color = Gray700,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .then(if (minHeight != Dp.Unspecified) Modifier.heightIn(min = minHeight) else Modifier),
            placeholder = { Text(placeholder, color = Gray700.copy(alpha = 0.5f)) },
            singleLine = singleLine,
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
