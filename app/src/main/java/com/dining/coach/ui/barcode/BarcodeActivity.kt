package com.dining.coach.ui.barcode

import android.os.Bundle
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)

    }
}