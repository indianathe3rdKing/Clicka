package com.example.clicka.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.clicka.ui.extensions.TopBar
import com.example.clicka.config.data.*
import com.example.clicka.ui.extensions.components.SaveButton
import com.example.clicka.ui.extensions.components.SettingsSection
import com.example.clicka.ui.extensions.components.SettingsSwitchItem
import com.example.clicka.ui.extensions.components.SettingsTextField

object SettingsTab: Tab {
    @Suppress("unused")
    private fun readResolve(): Any = SettingsTab

    override val options: TabOptions

        @Composable
        get(){
            val title = "Settings"
            val icon = rememberVectorPainter(Icons.Default.Settings)

            return remember{
                TabOptions(
                    index = 0u,
                    title=title,
                    icon=icon
                )
            }
        }

    @Composable
    override fun Content() {
        SettingsScreen()
    }

}

@Composable
private fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getConfigPreferences() }

    // Click Settings
    var clickPressDuration by remember { mutableStateOf(prefs.getClickPressDurationConfig(50L).toString()) }
    var clickRepeatCount by remember { mutableStateOf(prefs.getClickRepeatCountConfig(1).toString()) }
    var clickRepeatDelay by remember { mutableStateOf(prefs.getClickRepeatDelayConfig(100L).toString()) }

    // Swipe Settings
    var swipeDuration by remember { mutableStateOf(prefs.getSwipeDurationConfig(300L).toString()) }
    var swipeRepeatCount by remember { mutableStateOf(prefs.getSwipeRepeatCountConfig(1).toString()) }
    var swipeRepeatDelay by remember { mutableStateOf(prefs.getSwipeRepeatDelayConfig(100L).toString()) }

    // General Settings
    var pauseDuration by remember { mutableStateOf(prefs.getPauseDurationConfig(1000L).toString()) }
    var randomize by remember { mutableStateOf(prefs.getRandomizeConfig(true)) }

    var showSaveMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)

    ) {
        TopBar("Settings")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Click Settings Section
            SettingsSection(title = "Click Settings") {
                SettingsTextField(
                    value = clickPressDuration,
                    onValueChange = { if (it.all(Char::isDigit)) clickPressDuration = it },
                    label = "Press Duration (ms)",
                    description = "How long to hold each click"
                )

                SettingsTextField(
                    value = clickRepeatCount,
                    onValueChange = { if (it.all(Char::isDigit)) clickRepeatCount = it },
                    label = "Repeat Count",
                    description = "Number of times to click per button"
                )

                SettingsTextField(
                    value = clickRepeatDelay,
                    onValueChange = { if (it.all(Char::isDigit)) clickRepeatDelay = it },
                    label = "Repeat Delay (ms)",
                    description = "Delay between repeated clicks"
                )
            }

            // Swipe Settings Section
            SettingsSection(title = "Swipe Settings") {
                SettingsTextField(
                    value = swipeDuration,
                    onValueChange = { if (it.all(Char::isDigit)) swipeDuration = it },
                    label = "Swipe Duration (ms)",
                    description = "How long the swipe gesture takes"
                )

                SettingsTextField(
                    value = swipeRepeatCount,
                    onValueChange = { if (it.all(Char::isDigit)) swipeRepeatCount = it },
                    label = "Repeat Count",
                    description = "Number of times to repeat swipe"
                )

                SettingsTextField(
                    value = swipeRepeatDelay,
                    onValueChange = { if (it.all(Char::isDigit)) swipeRepeatDelay = it },
                    label = "Repeat Delay (ms)",
                    description = "Delay between repeated swipes"
                )
            }

            // General Settings Section
            SettingsSection(title = "General Settings") {
                SettingsTextField(
                    value = pauseDuration,
                    onValueChange = { if (it.all(Char::isDigit)) pauseDuration = it },
                    label = "Cycle Pause (ms)",
                    description = "Pause duration between action cycles"
                )

                SettingsSwitchItem(
                    checked = randomize,
                    onCheckedChange = { randomize = it },
                    label = "Randomize Timing",
                    description = "Add random variations to bypass anti-bot detection"
                )
            }

            // Save Button
            SaveButton(
                onClick = {
                    prefs.edit()
                        .putClickPressDurationConfig(clickPressDuration.toLongOrNull() ?: 50L)
                        .putClickRepeatCountConfig(clickRepeatCount.toIntOrNull() ?: 1)
                        .putClickRepeatDelayConfig(clickRepeatDelay.toLongOrNull() ?: 100L)
                        .putSwipeDurationConfig(swipeDuration.toLongOrNull() ?: 300L)
                        .putSwipeRepeatCountConfig(swipeRepeatCount.toIntOrNull() ?: 1)
                        .putSwipeRepeatDelayConfig(swipeRepeatDelay.toLongOrNull() ?: 100L)
                        .putPauseDurationConfig(pauseDuration.toLongOrNull() ?: 1000L)
                        .putRandomizeConfig(randomize)
                        .apply()
                    showSaveMessage = true
                }
            )

            if (showSaveMessage) {
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    showSaveMessage = false
                }
                Text(
                    "Settings saved successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}