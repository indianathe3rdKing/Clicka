package com.example.clicka.services.overlaylifecycleowner

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

class OverlayLifecyeOwner: SavedStateRegistryOwner {
    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private val mSavedStateRegisterController: SavedStateRegistryController =
        SavedStateRegistryController.create(this)


    override val savedStateRegistry: SavedStateRegistry
        get() = mSavedStateRegisterController.savedStateRegistry
    override val lifecycle: Lifecycle
        get() = mLifecycleRegistry


    fun setCureentState(state: Lifecycle.State){
        mLifecycleRegistry.currentState=state
    }
    fun handleLifecycleEvent(event: Lifecycle.Event){
        mLifecycleRegistry.handleLifecycleEvent(event)
    }
    fun performRestore(savedState: Bundle?){
        mSavedStateRegisterController.performRestore(savedState)
    }
    fun performSave(outBundle: Bundle){
        mSavedStateRegisterController.performSave(outBundle)
    }

}