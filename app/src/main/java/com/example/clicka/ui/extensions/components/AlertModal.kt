package com.example.clicka.ui.extensions.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun AlertModal(onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = {},
        title = {Text("Mode Required")},
        text = {Text("To start using the auto click feature, you must select a mode.")},
        confirmButton = {TextButton(
            onClick = {onDismiss()},
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = MaterialTheme.shapes.medium
        ) {Text("OK", color = MaterialTheme.colorScheme.inverseOnSurface) }}
        )
}