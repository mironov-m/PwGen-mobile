package ru.mironov.pwgen.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mironov.pwgen.ui.AndroidClipboardManager
import ru.mironov.pwgen.ui.ClipboardManager

actual val platformModule: Module = module {
    single<ClipboardManager> {
        AndroidClipboardManager(androidContext())
    }
}