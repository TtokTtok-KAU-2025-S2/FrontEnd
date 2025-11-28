package com.kau.ttokttok.ui.component.noisevote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.domain.model.board.noisevote.NoiseVoteType
import com.kau.ttokttok.ui.theme.*

@Composable
fun VoteButton(
    modifier: Modifier = Modifier,
    label: String,
    count: Int,
    selected: Boolean,
    selectedBg: Color,
    selectedBorder: Color,
    selectedText: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onButtonClick: () -> Unit,
) {
    androidx.compose.material3.OutlinedButton(
        onClick = {
            onButtonClick
        },
        modifier = modifier
            .height(90.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) selectedBg else Color.Transparent,
            contentColor = if (selected) selectedText else Gray600
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) selectedBorder else Gray400.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 11.sp)
            Text("$count", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}