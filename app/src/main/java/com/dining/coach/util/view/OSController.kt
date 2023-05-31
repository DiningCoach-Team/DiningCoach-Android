package com.dining.coach.util.view

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import com.dining.coach.R
import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name

interface OSController {
    fun setOsBarsColor(activity: Activity, iconColor: IconColor) {
        val decorView = activity.window.decorView
        val color = when (iconColor) {
            IconColor.WHITE -> {
                ContextCompat.getColor(activity, R.color.white)
            }

            IconColor.BLACK -> {
                ContextCompat.getColor(activity, R.color.black)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when (iconColor) {
                IconColor.WHITE -> {
                    decorView.windowInsetsController!!.apply {
                        setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                        setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                        )
                    }
                }

                IconColor.BLACK -> {
                    decorView.windowInsetsController!!.apply {
                        setSystemBarsAppearance(0,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                        setSystemBarsAppearance(0,
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                        )
                    }
                }
            }

        } else {
            when (iconColor) {
                IconColor.WHITE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    } else {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }

                IconColor.BLACK -> {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }

        activity.window.statusBarColor = color
        activity.window.navigationBarColor = color
    }

    fun setPrimaryOSBars(activity: Activity) {
        val decorView = activity.window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.windowInsetsController!!.apply {
                setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
                setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.primary_500)
        activity.window.navigationBarColor = ContextCompat.getColor(activity, R.color.primary_500)
    }

    enum class IconColor {
        WHITE,
        BLACK
    }
}