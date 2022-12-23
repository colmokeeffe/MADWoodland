package org.wit.woodland.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.woodland.models.*
import org.wit.woodland.models.firebase.WoodlandFireStore


class MainApp : Application(), AnkoLogger {

    lateinit var woodlands: WoodlandStore
    //entry pointy into woodlands a
    override fun onCreate()
    {
        super.onCreate()
        woodlands = WoodlandFireStore(applicationContext)
        info("Woodland started")
    }
}