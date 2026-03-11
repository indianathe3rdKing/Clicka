package com.example.clicka.ui.extensions.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun AlertModal(onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = {},
        title = {Text("Mode Required")},
        text = {Text("To start using the auto click feature, you must select a mode.")},
        confirmButton = {TextButton(
            onClick = {onDismiss()}
        ) {Text("OK") }}
        )
}