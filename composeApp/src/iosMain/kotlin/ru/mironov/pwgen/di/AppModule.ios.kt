package ru.mironov.pwgen.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mironov.pwgen.ui.ClipboardManager
import ru.mironov.pwgen.ui.IOSClipboardManager

actual val platformModule: Module = module {
    single<ClipboardManager> {
        IOSClipboardManager()
    }
}