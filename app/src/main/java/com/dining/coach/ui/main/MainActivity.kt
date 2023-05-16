package com.dining.coach.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.util.const.DEBUG_MODE
import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DEBUG(this@MainActivity.name, "main Listener()")
    }
}