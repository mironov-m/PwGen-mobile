package ru.mironov.pwgen.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.mironov.pwgen.ui.screens.main.presentation.MainViewModel

val presentationModule = module {
    viewModel { MainViewModel(get()) }
}