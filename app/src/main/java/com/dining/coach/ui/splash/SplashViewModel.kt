package com.dining.coach.ui.splash

import com.dining.coach.base.BaseViewModel
import com.dining.coach.util.const.DEBUG_MODE
import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(

): BaseViewModel() {
    init {
        DEBUG(this@SplashViewModel.name, "SplashViewModel init!!")
    }
}