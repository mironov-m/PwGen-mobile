package ru.mironov.pwgen

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.jetpack.ProvideNavigatorLifecycleKMPSupport
import cafe.adriel.voyager.navigator.Navigator
import ru.mironov.pwgen.di.initKoin
import ru.mironov.pwgen.ui.screens.main.MainScreen

// Lazy initialization ensures Koin is initialized only once
private val koinInitializer: Unit by lazy {
    initKoin()
}

@OptIn(ExperimentalVoyagerApi::class)
fun MainViewController() = ComposeUIViewController {
    // Initialize Koin before showing UI
    koinInitializer
    ProvideNavigatorLifecycleKMPSupport {
        Navigator(MainScreen())
    }
}