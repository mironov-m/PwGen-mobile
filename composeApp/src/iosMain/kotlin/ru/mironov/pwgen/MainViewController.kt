package ru.mironov.pwgen

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.navigator.Navigator
import ru.mironov.pwgen.di.initKoin
import ru.mironov.pwgen.ui.screens.main.MainScreen

// Lazy initialization ensures Koin is initialized only once
private val koinInitializer: Unit by lazy {
    initKoin()
}

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin before showing UI
    koinInitializer
    Navigator(MainScreen())
}