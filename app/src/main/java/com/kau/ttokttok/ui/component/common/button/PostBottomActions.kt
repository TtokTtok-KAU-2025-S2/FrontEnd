package com.kau.ttokttok.ui.component.common.button

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.ui.theme.*

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