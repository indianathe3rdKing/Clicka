package com.indiphile_menziwa.clicka.ui.extensions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indiphile_menziwa.clicka.R
import com.indiphile_menziwa.clicka.ui.extensions.components.TutorialImage
import com.indiphile_menziwa.clicka.ui.extensions.components.TutorialStepHeader


@Composable
fun TutorialCards(step: String,caption: String,imagineId: Int){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TutorialStepHeader(step,caption)
            TutorialImage(ImageResourceId = imagineId)
        }
    }
}