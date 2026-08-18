package com.indiphile_menziwa.clicka.ui.extensions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indiphile_menziwa.clicka.R

@Composable
fun TutorialImage(ImageResourceId: Int = R.drawable.step1) {
    Image(painter = painterResource(ImageResourceId), contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .border(width = 2.dp,color= MaterialTheme.colorScheme.outline, shape = MaterialTheme.shapes.medium),
        contentScale = ContentScale.Fit)
}