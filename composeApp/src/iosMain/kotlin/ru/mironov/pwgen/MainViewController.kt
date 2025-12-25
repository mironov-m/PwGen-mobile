package ru.mironov.pwgen

import androidx.compose.ui.window.ComposeUIViewController
import ru.mironov.pwgen.di.initKoin

// Lazy initialization ensures Koin is initialized only once
private val koinInitializer: Unit by lazy {
    initKoin()
}

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin before showing UI
    koinInitializer
    App()
}