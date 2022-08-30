package com.example.ep3faith

import android.app.Application
import timber.log.Timber

class FaithApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
