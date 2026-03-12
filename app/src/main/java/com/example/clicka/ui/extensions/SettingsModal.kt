package com.example.clicka.ui.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clicka.ui.extensions.components.SettingsSwitchItem
import com.example.clicka.ui.theme.BorderColor
import com.example.clicka.ui.theme.PrimaryText
import com.example.clicka.ui.theme.TranslucentBackground

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
    var randomize by rememberSaveable { mutableStateOf(initialSettings.randomize) }

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
                label = { Text("Press Duration (ms)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    ,
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor  = MaterialTheme.colorScheme.background

                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = repeatDelayText,
                onValueChange = {
                    if (it.all(Char::isDigit)) repeatDelayText = it
                },

                label = { Text("Repeat Delay (ms)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                ,
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor  = MaterialTheme.colorScheme.background

                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = repeatCountText,
                onValueChange = {
                    if (it.all(Char::isDigit)) repeatCountText = it
                },

                label = { Text("Repeat Count", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                ,
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor  = MaterialTheme.colorScheme.background

                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = cycleDelayText,
                onValueChange = {
                    if (it.all(Char::isDigit)) cycleDelayText = it
                },

                label = { Text("Cycle Delay (ms)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                ,
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.6f),
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor  = MaterialTheme.colorScheme.background

                )
            )

            Spacer(Modifier.height(12.dp))

            SettingsSwitchItem(
                checked = randomize,
                onCheckedChange = {randomize=it},
                label = "Randomize Timing",
                description = "Add random variations to bypass anti-bot detection"
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
                            randomize = randomize
                        )
                        onSave(settings)
                        onClose()
                    },
                    shape= MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                ) {
                    Text("Save", color = MaterialTheme.colorScheme.onSurface)
                }

                Button(
                    onClick = { onClose() },
                    shape= MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}