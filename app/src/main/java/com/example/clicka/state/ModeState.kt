package com.example.clicka.state

import com.example.clicka.domain.model.ClickMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton object to hold the selected click mode state.
 * This allows sharing state between the UI (ViewModel/Compose) and the OverlayService.
 */
object ModeState {
    private val _selectedMode = MutableStateFlow(ClickMode.SINGLE)
    val selectedMode: StateFlow<ClickMode> = _selectedMode.asStateFlow()

    private val _modeSelected = MutableStateFlow(false)
    val modeSelected: StateFlow<Boolean> = _modeSelected.asStateFlow()

    fun updateMode(mode: ClickMode) {
        _selectedMode.value = mode
        _modeSelected.value = true
    }

    fun getCurrentMode(): ClickMode = _selectedMode.value
}

