package com.dining.coach.ui.barcode

import android.os.Bundle
import androidx.activity.viewModels
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivityBarcodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeActivity : BaseActivity<ActivityBarcodeBinding>(R.layout.activity_barcode) {
    private val viewModel: BaseViewModel by viewModels()

    override fun createActivity() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
    }

}