package com.dining.coach.ui.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun createActivity(): BaseViewModel {
        return viewModel
    }
}