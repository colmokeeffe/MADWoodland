package org.wit.woodland.main

import android.app.Application
import org.wit.woodland.models.WoodlandJSONStore
import org.wit.woodland.models.WoodlandStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var woodlands: WoodlandStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        woodlands = WoodlandJSONStore(applicationContext)
        i("Woodland started")
    }
}