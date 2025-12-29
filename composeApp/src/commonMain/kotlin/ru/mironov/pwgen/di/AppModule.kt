package ru.mironov.pwgen.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mironov.pwgen.domain.PasswordGenerator
import ru.mironov.pwgen.domain.PasswordGeneratorSecureRandomImpl

val appModule = module {

    single<PasswordGenerator> {
        PasswordGeneratorSecureRandomImpl()
    }
}

expect val platformModule: Module