package com.dining.coach.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}