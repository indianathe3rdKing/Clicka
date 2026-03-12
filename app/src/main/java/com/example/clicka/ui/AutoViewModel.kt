package com.example.clicka.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicka.domain.model.ClickMode
import com.example.clicka.state.ModeState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AutoViewModel: ViewModel() {

    private var _selectedMode = mutableStateOf(ModeState.getCurrentMode())
    val selectedMode: State<ClickMode> = _selectedMode

    private var _modeSelected = mutableStateOf(ModeState.modeSelected.value)
    val modeSelected: State<Boolean> = _modeSelected

    private var _isRunning = mutableStateOf(ModeState.isRunning.value)
    val isRunning: State<Boolean> = _isRunning


    init {
        // Observe the shared ModeState and update local state
        ModeState.selectedMode
            .onEach { mode -> _selectedMode.value = mode }
            .launchIn(viewModelScope)

        ModeState.modeSelected
            .onEach { selected -> _modeSelected.value = selected }
            .launchIn(viewModelScope)
        ModeState.isRunning
            .onEach { isRunning -> _isRunning.value = isRunning }
            .launchIn(viewModelScope)
    }

    fun updateMode(mode: ClickMode) {
        // Update the shared singleton state
        ModeState.updateMode(mode)
    }
}
