package com.hekmatullahamin.littlelemon

import android.app.Application
import com.hekmatullahamin.littlelemon.data.AppContainer
import com.hekmatullahamin.littlelemon.data.AppContainerImpl

class LittleLemonApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}