package ru.mironov.pwgen.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initialize Koin dependency injection.
 * Should be called once at application startup.
 *
 * @param appDeclaration Optional platform-specific configuration
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            appModule,
            presentationModule,
        )
    }
}