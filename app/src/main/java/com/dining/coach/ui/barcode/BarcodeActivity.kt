package com.dining.coach.ui.barcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivityBarcodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeActivity : BaseActivity<ActivityBarcodeBinding>(R.layout.activity_barcode) {
    private val viewModel: BaseViewModel by viewModels()
    private lateinit var cameraController: LifecycleCameraController
    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA)

    override fun createActivity() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        processStartCamera()
    }


    private fun processStartCamera(){
        if(hasAllPermissions()){
            startCamera()
        }else{
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { isGranted ->
                if (isGranted.all { it.value }) {
                    startCamera()
                } else {
                    // TODO("replace permission denied process")
                    Toast.makeText(this, "permission error", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.launch(requiredPermissions)
        }
    }

    private fun startCamera() {
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        bind.preview.controller = cameraController
    }
    private fun stopCamera() {
        cameraController.unbind()
    }

    private fun hasAllPermissions() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

}