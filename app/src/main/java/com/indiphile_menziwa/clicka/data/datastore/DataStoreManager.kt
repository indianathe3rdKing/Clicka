package com.indiphile_menziwa.clicka.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStoreManager by preferencesDataStore(name = "clicka_preferences")
class DataStoreManager(private val context: Context) {
    companion object{
        val AGREEMENTACCEPTED_KEY = booleanPreferencesKey("agreement_key")
    }


    suspend fun setAgreementAccepted(agreementAccepted: Boolean){
        context.dataStoreManager.edit{
            prefs-> prefs[AGREEMENTACCEPTED_KEY]=agreementAccepted
        }
    }

    val agreementAccepted: Flow<Boolean> = context.dataStoreManager.data.map {
        prefs-> prefs[AGREEMENTACCEPTED_KEY]?:false
    }

    fun getAgreementAcceptedSync(): Boolean = runBlocking {
        context.dataStoreManager.data.first()[AGREEMENTACCEPTED_KEY] ?: false
    }

}