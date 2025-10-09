package com.kau.ttokttok.ui.component.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    dismissText: String = "확인"
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },

        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },

        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        },

        tonalElevation = 8.dp
    )
}