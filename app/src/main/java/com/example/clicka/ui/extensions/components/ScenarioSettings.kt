package com.example.clicka.ui.extensions.components

import com.example.clicka.ui.extensions.AutoClickSettings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScenarioSettings(settings: AutoClickSettings) {

    var delayText by remember { mutableStateOf(settings.repeatDelayMs.toString()) }
    var intervalText by rememberSaveable { mutableStateOf(settings.cycleDelayMs.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Settings")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = delayText,
                onValueChange = {
                    delayText = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = intervalText,
                onValueChange = {
                    intervalText = it
                }
            )
        }
    }
}