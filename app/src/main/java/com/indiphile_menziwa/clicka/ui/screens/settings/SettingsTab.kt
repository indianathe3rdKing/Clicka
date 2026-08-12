package com.indiphile_menziwa.clicka.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.indiphile_menziwa.clicka.R
import com.indiphile_menziwa.clicka.ui.extensions.TopBar
import com.indiphile_menziwa.clicka.config.data.*
import com.indiphile_menziwa.clicka.ui.extensions.components.SaveButton
import com.indiphile_menziwa.clicka.ui.extensions.components.SettingsSection
import com.indiphile_menziwa.clicka.ui.extensions.components.SettingsSwitchItem
import com.indiphile_menziwa.clicka.ui.extensions.components.SettingsTextField
import kotlinx.coroutines.delay

object SettingsTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = SettingsTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.settings)
            val icon = painterResource(R.drawable.settings_30)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
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
    var clickPressDuration by remember {
        mutableStateOf(
            prefs.getClickPressDurationConfig(50L).toString()
        )
    }
    var clickRepeatCount by remember {
        mutableStateOf(
            prefs.getClickRepeatCountConfig(1).toString()
        )
    }
    var clickRepeatDelay by remember {
        mutableStateOf(
            prefs.getClickRepeatDelayConfig(100L).toString()
        )
    }

    // Swipe Settings
    var swipeDuration by remember { mutableStateOf(prefs.getSwipeDurationConfig(300L).toString()) }
    var swipeRepeatCount by remember {
        mutableStateOf(
            prefs.getSwipeRepeatCountConfig(1).toString()
        )
    }
    var swipeRepeatDelay by remember {
        mutableStateOf(
            prefs.getSwipeRepeatDelayConfig(100L).toString()
        )
    }

    // General Settings
    var pauseDuration by remember { mutableStateOf(prefs.getPauseDurationConfig(1000L).toString()) }
    var randomize by remember { mutableStateOf(prefs.getRandomizeConfig(true)) }

    var showSaveMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)

    ) {
        TopBar(stringResource(R.string.settings))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Click Settings Section
            SettingsSection(title = stringResource(R.string.click_settings_title)) {
                SettingsTextField(
                    value = clickPressDuration,
                    onValueChange = { if (it.all(Char::isDigit)) clickPressDuration = it },
                    label = stringResource(R.string.press_duration_title),
                    description = stringResource(R.string.press_duration_description)
                )

                SettingsTextField(
                    value = clickRepeatCount,
                    onValueChange = { if (it.all(Char::isDigit)) clickRepeatCount = it },
                    label = stringResource(R.string.repeat_count_title),
                    description = stringResource(R.string.repeat_count_description)
                )

                SettingsTextField(
                    value = clickRepeatDelay,
                    onValueChange = { if (it.all(Char::isDigit)) clickRepeatDelay = it },
                    label = stringResource(R.string.repeat_delay_title),
                    description = stringResource(R.string.repeat_delay_description)
                )
            }

            // Swipe Settings Section
            SettingsSection(title = stringResource(R.string.swipe_settings)) {
                SettingsTextField(
                    value = swipeDuration,
                    onValueChange = { if (it.all(Char::isDigit)) swipeDuration = it },
                    label = stringResource(R.string.swipe_duration_title),
                    description = stringResource(R.string.swipe_duration_description)
                )

                SettingsTextField(
                    value = swipeRepeatCount,
                    onValueChange = { if (it.all(Char::isDigit)) swipeRepeatCount = it },
                    label = stringResource(R.string.repeat_count_title),
                    description = stringResource(R.string.repeat_count_description_swipe)
                )

                SettingsTextField(
                    value = swipeRepeatDelay,
                    onValueChange = { if (it.all(Char::isDigit)) swipeRepeatDelay = it },
                    label = stringResource(R.string.repeat_delay_title),
                    description = stringResource(R.string.repeat_count_description_swipe)
                )
            }

            // General Settings Section
            SettingsSection(title = stringResource(R.string.general_settings_title)) {
                SettingsTextField(
                    value = pauseDuration,
                    onValueChange = { if (it.all(Char::isDigit)) pauseDuration = it },
                    label = stringResource(R.string.cycle_pause_title),
                    description = stringResource(R.string.cycle_pause_description)
                )

                SettingsSwitchItem(
                    checked = randomize,
                    onCheckedChange = { randomize = it },
                    label = stringResource(R.string.randomize_timing_title),
                    description = stringResource(R.string.randomize_timing_description)
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
                    delay(2000)
                    showSaveMessage = false
                }
                Text(
                    stringResource(R.string.save_text),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(90.dp))
        }
    }
}