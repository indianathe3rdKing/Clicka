package com.example.clicka.extensions

import android.content.Context
import android.icu.text.CaseMap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.clicka.overlayPermission


@Composable
internal fun OverviewHeader(
    title: String,

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            title, style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)

        )
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.shapes.large

                ),
            containerColor = Color.Transparent
        ) {
            Icon(imageVector = Icons.Filled.Add, null, tint = MaterialTheme.colorScheme.onSurface)
        }
    }

}