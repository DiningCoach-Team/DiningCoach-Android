package com.diningcoach.data.util

import android.util.Log
import com.diningcoach.data.BuildConfig

fun DEBUG(tag: String, contents: String) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, contents)
    }
}

fun INFO(tag: String, contents: String) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, contents)
    }
}

fun N_INFO(contents: String) {
    if (BuildConfig.DEBUG) {
        Log.i("RETROFIT", contents)
    }
}

fun ERROR(tag: String, contents: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, contents)
    }
}

val Any.name: String get() = this.javaClass.simpleName