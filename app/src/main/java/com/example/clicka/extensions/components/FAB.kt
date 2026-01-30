package com.example.clicka.extensions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clicka.index.FABInfo

@Composable
private fun FAB(fabInfo: FABInfo) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.Transparent
    ) {
        FloatingActionButton(
            onClick = fabInfo.onClick,
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ),
            modifier = Modifier
                .padding(6.dp)
                .size(50.dp)
        ) {
            Icon(
                painter = painterResource(fabInfo.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(Color.Transparent)
            )
        }
    }
}

@Composable
internal fun fabComponent(
    fabInfo: List<FABInfo>
) {
    LazyColumn {
        items(fabInfo) { fab ->
            FAB(fab)
        }
    }
}
