package com.indiphile_menziwa.clicka.ui.extensions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.indiphile_menziwa.clicka.ui.theme.TranslucentBackground

@Composable
fun TutorialStepHeader(step: String, caption: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.background)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline.copy(0.6f),
                    shape = MaterialTheme.shapes.small
                )
                ,
            contentAlignment = Alignment.Center

        ) {
            Text(
                step,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(caption,

            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold)

    }
}