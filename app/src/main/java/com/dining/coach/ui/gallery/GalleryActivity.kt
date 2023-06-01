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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryActivity : BaseActivity() {
    private val viewModel: GalleryViewModel by viewModels()

    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val requestPermissionsLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            if (isGranted.all { it.value }) {
                // TODO("replace coroutine scope")
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.fetchImages(this@GalleryActivity)
                }
            } else {
                // TODO("replace permission denied process")
                Toast.makeText(this, "permission error", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        processGetGalleryData()
    }

    private fun processGetGalleryData(){
        if (hasAllPermissions()) {
            // TODO("replace coroutine scope")
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchImages(this@GalleryActivity)
            }
        } else {
            requestPermissionsLauncher.launch(requiredPermissions)
        }
    }

    private fun hasAllPermissions() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

}
