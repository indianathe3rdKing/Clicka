package com.indiphile_menziwa.clicka.ui.screens.select.selectContent

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.indiphile_menziwa.clicka.index.ButtonInfoProvider
import com.indiphile_menziwa.clicka.ui.DataStoreViewModel
import com.indiphile_menziwa.clicka.ui.extensions.ButtonComponent
import com.indiphile_menziwa.clicka.ui.extensions.TopBar
import com.indiphile_menziwa.clicka.ui.extensions.components.AlertModalConsent
import com.indiphile_menziwa.clicka.ui.screens.tutorial.TutorialScreen

object SelectTabContent: Screen {


    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: DataStoreViewModel = viewModel(factory = DataStoreViewModel.factory(context))
        val agreementAccepted by viewModel.isAgreementAccepted.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 30.dp)
        ) {
            if (!agreementAccepted) {
                AlertModalConsent(
                    onDismiss = { (context as? Activity)?.finish() },
                    onAccept = { viewModel.updateAgreementAcceptance(true) },
                    onTutorial = { navigator.push(TutorialScreen()) }
                )
            }
            TopBar("Clicka")
            ButtonComponent(ButtonInfoProvider.samples)

        }
    }

}