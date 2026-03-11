package com.example.clicka.ui.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clicka.overlayPermission
import com.example.clicka.state.ModeState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.clicka.ui.extensions.components.AlertModal


@Composable
internal fun TopBar(
    title: String,

    ) {
    val context = LocalContext.current



Row(
verticalAlignment = Alignment.CenterVertically,
horizontalArrangement = Arrangement.SpaceBetween,
modifier = Modifier
.padding(16.dp)
.fillMaxWidth()
.wrapContentHeight()
) {
    var showDialog by remember { mutableStateOf(false) }
    val modeSelected = ModeState.modeSelected.collectAsState().value
    if (showDialog) {
        AlertModal(onDismiss = { showDialog = false })
    }
    Text(
        title, style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f),
        fontWeight = FontWeight.Bold,
        letterSpacing = (1.2).sp

    )
    FloatingActionButton(
        onClick = {
            if (modeSelected) overlayPermission(context)
            else showDialog = true
        },
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.large

            ), elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        containerColor = Color.Transparent
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

}