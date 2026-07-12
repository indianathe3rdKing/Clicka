package com.indiphile_menziwa.clicka.ui.extensions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
internal fun TextBox(){
    var state by rememberSaveable{mutableStateOf("")}
    Box(
        modifier = Modifier
            .padding(16.dp)

    ) {
        OutlinedTextField(
            value = state,
            onValueChange = {state = it},
            modifier = Modifier
                .width(100.dp)
                .height(60.dp),
            singleLine = true,
            label={Text("Interval")}
        )
    }

}