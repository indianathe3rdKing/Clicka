package com.example.clicka.ui.extensions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicka.R
import com.example.clicka.index.FABInfo
import com.example.clicka.ui.AutoViewModel
import com.example.clicka.ui.theme.PrimaryText

@Composable
private fun FAB(fabInfo: FABInfo, viewModel: AutoViewModel) {
    val icon: Int = R.drawable.play

    val runValue by viewModel.isRunning
    val isRunning = runValue && fabInfo.icon == icon

    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.Transparent, modifier = Modifier
            .padding(top = 4.dp)
    ) {
        FloatingActionButton(
            onClick = fabInfo.onClick,

            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ),
            modifier = Modifier
                .padding(0.dp) // reduced padding so FABs are closer
                .size(36.dp),
            containerColor =
                if (isRunning) MaterialTheme.colorScheme.onSurface else Color.Transparent
        ) {

            Icon(
                painter = painterResource(fabInfo.icon),
                contentDescription = null,
                tint = if (isRunning) MaterialTheme.colorScheme.inverseOnSurface.copy(0.86f) else PrimaryText,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
            )
        }
    }
}

@Suppress("ComposableNaming")
@Composable
internal fun fabComponent(
    fabInfo: List<FABInfo>
) {
    val viewModel :AutoViewModel = viewModel()
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp), // remove extra spacing
        contentPadding = PaddingValues(0.dp)
    ) {
        items(fabInfo) { fab ->
            FAB(fab, viewModel)
        }
    }
}
