package com.example.clicka.ui.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clicka.ui.theme.BorderColor

/**
 * Data class to hold auto-click settings (maps to existing click config preferences)
 */
data class AutoClickSettings(
    val pressDurationMs: Long = 50L,       // Maps to click press duration
    val repeatDelayMs: Long = 100L,         // Maps to click repeat delay
    val repeatCount: Int = 1,
    val cycleDelayMs: Long = 1000L,         // Maps to pause duration (delay between cycles)
    val randomize: Boolean = true            // Randomize timing for anti-bot bypass
)

@Composable
internal fun SettingsModal(
    initialSettings: AutoClickSettings = AutoClickSettings(),
    onSave: (AutoClickSettings) -> Unit = {},
    onClose: () -> Unit
) {
    var pressDurationText by rememberSaveable { mutableStateOf(initialSettings.pressDurationMs.toString()) }
    var repeatDelayText by rememberSaveable { mutableStateOf(initialSettings.repeatDelayMs.toString()) }
    var repeatCountText by rememberSaveable { mutableStateOf(initialSettings.repeatCount.toString()) }
    var cycleDelayText by rememberSaveable { mutableStateOf(initialSettings.cycleDelayMs.toString()) }
    var randomizeText by rememberSaveable { mutableStateOf(initialSettings.randomize.toString()) }

    Card(
        onClick = {},
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(98, 97, 97, 52))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Click Settings",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = pressDurationText,
                onValueChange = {
                    if (it.all(Char::isDigit)) pressDurationText = it
                },
                label = { Text("Press Duration (ms)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = BorderColor
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = repeatDelayText,
                onValueChange = {
                    if (it.all(Char::isDigit)) repeatDelayText = it
                },
                label = { Text("Repeat Delay (ms)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = BorderColor
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = repeatCountText,
                onValueChange = {
                    if (it.all(Char::isDigit)) repeatCountText = it
                },
                label = { Text("Repeat Count") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = BorderColor
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = cycleDelayText,
                onValueChange = {
                    if (it.all(Char::isDigit)) cycleDelayText = it
                },
                label = { Text("Cycle Delay (ms)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = BorderColor
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = randomizeText,
                onValueChange = {
                    if (it.all(Char::isDigit)) randomizeText = it
                },
                label = { Text("Randomize (true/false)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = BorderColor
                )
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val settings = AutoClickSettings(
                            pressDurationMs = pressDurationText.toLongOrNull() ?: 50L,
                            repeatDelayMs = repeatDelayText.toLongOrNull() ?: 100L,
                            repeatCount = (repeatCountText.toIntOrNull() ?: 1).coerceAtLeast(1),
                            cycleDelayMs = cycleDelayText.toLongOrNull() ?: 1000L,
                            randomize = randomizeText.toBoolean()
                        )
                        onSave(settings)
                        onClose()
                    }
                ) {
                    Text("Save")
                }

                Button(
                    onClick = { onClose() }
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}