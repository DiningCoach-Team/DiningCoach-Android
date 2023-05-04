package com.dining.coach.util.debug

import android.util.Log
import com.dining.coach.util.const.DEBUG_MODE

fun DEBUG(tag: String, contents: String) {
    if (DEBUG_MODE) {
        Log.d(tag, contents)
    }
}

fun INFO(tag: String, contents: String) {
    if (DEBUG_MODE) {
        Log.i(tag, contents)
    }
}

fun ERROR(tag: String, contents: String) {
    if (DEBUG_MODE) {
        Log.e(tag, contents)
    }
}

val Any.name: String get() = this.javaClass.simpleName