package com.dining.coach.ui.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivitySplashBinding
import com.dining.coach.ui.login.LoginActivity
import com.dining.coach.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun createActivity(): BaseViewModel {

        return viewModel
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) initViewModelCallback()
    }

    private fun initViewModelCallback() {
        viewModel.getOnMoveAction {
            lifecycleScope.launch {
                if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                    viewModel.isLogin.collectLatest { status ->
                        if (status) {
                            gotoActivityWithClear<MainActivity>()
                        } else {
                            gotoActivityWithClear<LoginActivity>()
                        }
                    }
                }
            }
        }
    }
}