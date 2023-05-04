package com.dining.coach.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class DCService: Service() {
    private var mBinder: IBinder? = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    inner class LocalBinder: Binder() {
        val service: DCService
            get() = this@DCService
    }
}