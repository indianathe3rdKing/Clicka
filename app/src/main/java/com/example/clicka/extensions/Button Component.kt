package com.example.clicka.extensions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clicka.index.ButtonInfo

@Composable
private fun ButtonCard(
    buttonInfo: ButtonInfo,
) {

    Card(
        onClick = {},
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.padding(16.dp,6.dp)
            .wrapContentHeight()

        ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.large),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(buttonInfo.icon),
                    contentDescription = buttonInfo.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .clip(MaterialTheme.shapes.large)

                )

            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    buttonInfo.title, style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    buttonInfo.description, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.86f)
                )

            }


        }
    }
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
internal fun ButtonComponent(
    buttonInfo: List<ButtonInfo>

) {
    LazyColumn {
        items(buttonInfo) { button ->
            ButtonCard(button)
        }
    }
}