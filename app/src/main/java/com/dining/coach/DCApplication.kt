package com.dining.coach

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.dining.coach.service.DCService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DCApplication:Application() {

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }

    override fun onCreate() {
        super.onCreate()

        startDCService()
    }

    private fun startDCService() {
        val intent = Intent(this@DCApplication, DCService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }
}