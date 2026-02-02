package com.example.clicka.extensions

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButtonElevation
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clicka.extensions.components.TextBox
import com.example.clicka.ui.theme.BorderColor
import com.example.clicka.ui.theme.GlassDark


@Composable
internal fun SettingsModal(onClose: () -> Unit) {

    var delayText by rememberSaveable { mutableStateOf("") }
    var intervalText by rememberSaveable { mutableStateOf("") }

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
                .padding(20.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            OutlinedTextField(
                value = delayText,
                onValueChange = {
                    if (it.all(Char::isDigit)) delayText = it
                },
                label = { Text("Delay (ms)") },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
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
                value = intervalText,
                onValueChange = {
                    if (it.all(Char::isDigit)) intervalText = it
                },
                label = { Text("Interval (min)") },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = BorderColor
                )
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    val delay = delayText.toLongOrNull() ?: 0L
                    val interval = intervalText.toLongOrNull() ?: 0L
                    onClose()
                }
            ) {
                Text("Close")
            }
        }
    }
}