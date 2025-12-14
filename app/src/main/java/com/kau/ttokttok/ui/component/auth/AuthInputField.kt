package com.kau.ttokttok.ui.component.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AuthInputField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    headerText: String,
    imageVector: ImageVector,
    exampleText: String
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = headerText,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    exampleText,
                    color = Color.White.copy(alpha = 0.6f) // placeholder:text-white/60
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f), // 아이콘 색
                    modifier = Modifier.size(18.dp)
                )
            },
            textStyle = LocalTextStyle.current.copy(color = Color.White), // 입력 텍스트 색
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)), // rounded-xl
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),   // bg-white/10
                unfocusedContainerColor = Color.White.copy(alpha = 0.1f), // bg-white/10
                disabledContainerColor = Color.White.copy(alpha = 0.1f),

                focusedIndicatorColor = Color.White.copy(alpha = 0.4f),   // focus:border-white/40
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f), // border-white/20

                cursorColor = Color.White,
                focusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                focusedTrailingIconColor = Color.White.copy(alpha = 0.6f),
                unfocusedTrailingIconColor = Color.White.copy(alpha = 0.6f)
            )
        )
    }
}