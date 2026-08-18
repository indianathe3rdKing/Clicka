package com.indiphile_menziwa.clicka.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.indiphile_menziwa.clicka.data.datastore.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer { DataStoreViewModel(DataStoreManager(context.applicationContext)) }
        }
    }

    val isAgreementAccepted = dataStoreManager.agreementAccepted
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = dataStoreManager.getAgreementAcceptedSync()
        )

    fun updateAgreementAcceptance(agreed: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setAgreementAccepted(agreed)
        }
    }
}