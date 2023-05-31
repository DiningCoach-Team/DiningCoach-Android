package com.dining.coach.ui.splash

import androidx.lifecycle.viewModelScope
import com.dining.coach.base.BaseViewModel
import com.diningcoach.domain.common.Resource
import com.diningcoach.domain.usecase.user.CheckIsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkIsLoginUseCase: CheckIsLoginUseCase
): BaseViewModel() {

    private var _isLogin = MutableStateFlow(false)
    val isLogin get() = _isLogin.asStateFlow()

    private var isPastTime = false

    private var onPast: (() -> Unit)? = null

    init {
        checkIsLogin()
    }

    fun getOnMoveAction(onPast: () -> Unit) {
        this@SplashViewModel.onPast = onPast

        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            isPastTime = true
        }
    }

    private fun checkIsLogin() {
        checkIsLoginUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLogin.value = result.data ?: false
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

}