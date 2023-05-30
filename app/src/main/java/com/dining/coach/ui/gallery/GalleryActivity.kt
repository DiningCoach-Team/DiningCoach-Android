package com.dining.coach.ui.gallery

import android.os.Bundle
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

    }
}