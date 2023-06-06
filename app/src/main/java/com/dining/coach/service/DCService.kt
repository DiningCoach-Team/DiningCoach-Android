package com.dining.coach.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.diningcoach.data.di.manager.DeviceNetworkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DCService: Service() {
    private var mBinder: IBinder? = LocalBinder()
    
    private var networkListener = object : DeviceNetworkManager.NetworkListener {
        override fun isOnline() {
            // TODO : Preference에 값 저장 할지 말지 고민 중입니다... :( 다음 기회에..
        }

        override fun isOffline() {
            // TODO : Preference에 값 저장 할지 말지 고민 중입니다... :(
        }
    }
    
    @Inject
    lateinit var deviceNetworkManager: DeviceNetworkManager

    override fun onCreate() {
        super.onCreate()
        deviceNetworkManager.register(networkListener)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        deviceNetworkManager.unregister()
    }

    inner class LocalBinder: Binder() {
        val service: DCService
            get() = this@DCService
    }
}