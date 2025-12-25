package ru.mironov.pwgen

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import ru.mironov.pwgen.di.initKoin

class PwGenApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {

            androidLogger(Level.ERROR)
            androidContext(this@PwGenApplication)
        }
    }
}