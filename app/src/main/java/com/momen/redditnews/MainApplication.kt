package com.momen.redditnews

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {

        lateinit var appContext: Context
            private set
    }
}