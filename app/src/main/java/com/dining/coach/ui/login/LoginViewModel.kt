package com.dining.coach.ui.login

import com.dining.coach.base.BaseViewModel
import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(

): BaseViewModel() {
    fun login() {
        DEBUG(this@LoginViewModel.name, "login()")
    }
}