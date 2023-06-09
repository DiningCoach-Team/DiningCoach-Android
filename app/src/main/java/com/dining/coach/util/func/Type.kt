package com.dining.coach.util.func

import android.content.res.Resources

val Int.dp: Int
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density + 0.5f).toInt()