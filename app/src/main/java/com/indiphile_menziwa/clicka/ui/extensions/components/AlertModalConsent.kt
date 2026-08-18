package com.indiphile_menziwa.clicka.ui.extensions.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AlertModalConsent(onAccept: () -> Unit, onDismiss: () -> Unit, onTutorial: () -> Unit) {

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Accessibility Required") },
        text = {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Clicka requires Accessibility Service permission to function normally.\n" +
                            "We use this permission to enable the automatic clicking feature.\n\nNo data is sold or shared with third parties."
                )
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { onTutorial() }
                ) {
                    Text(
                        text = "Tutorial",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                ),
                shape = MaterialTheme.shapes.medium
            ) { Text("Cancel", color = MaterialTheme.colorScheme.onSurface) }
        },
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