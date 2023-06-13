package com.dining.coach.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivityMainBinding
import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    override fun createActivity(): BaseViewModel {
        return viewModel
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}