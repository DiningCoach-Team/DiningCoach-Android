package com.dining.coach.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery) {
    private val viewModel: GalleryViewModel by viewModels()
    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun createActivity() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        processGetGalleryData()
    }

    private fun processGetGalleryData(){
        if (hasAllPermissions()) {
            fetchImages()
        } else {
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { isGranted ->
                if (isGranted.all { it.value }) {
                    fetchImages()
                } else {
                    // TODO("replace permission denied process")
                    Toast.makeText(this, "permission error", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.launch(requiredPermissions)
        }
    }


    private fun fetchImages(){
        // TODO("replace coroutine scope")
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.fetchImages(this@GalleryActivity)
        }
    }

    private fun hasAllPermissions() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

}
