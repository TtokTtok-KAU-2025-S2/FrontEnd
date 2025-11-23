package com.kau.ttokttok.ui.component.auth.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RegisterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), // h-12
        shape = RoundedCornerShape(12.dp), // rounded-xl
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.2f),   // bg-white/20
            contentColor = Color.White                        // text-white
        ),
        contentPadding = PaddingValues(0.dp) // Tailwind 기본처럼 꽉 채운 텍스트
    ) {
        Text(
            "회원가입",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}