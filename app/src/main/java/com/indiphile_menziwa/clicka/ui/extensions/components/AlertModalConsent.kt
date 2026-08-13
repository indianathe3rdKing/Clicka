package com.indiphile_menziwa.clicka.ui.extensions.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun AlertModalConsent(onAccept: () -> Unit,onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Accessibility Required") },
        text = {
            Text(
                "Clicka requires Accessibility Service permission to function normally.\n" +
                        "We use this permission to enable the automatic clicking feature.\n\nNo data is sold or shared with third parties."
            )
        },
        dismissButton = {TextButton(
            onClick = { onDismiss() },
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            shape = MaterialTheme.shapes.medium
        ) { Text("Cancel", color = MaterialTheme.colorScheme.onSurface) }},
        confirmButton = {
            TextButton(
                onClick = { onAccept() },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = MaterialTheme.shapes.medium
            ) { Text("Agree", color = MaterialTheme.colorScheme.inverseOnSurface) }
        }
    )
}