package com.example.qrcoderead

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView
import kotlinx.android.synthetic.main.activity_scanner.*
import kotlinx.android.synthetic.main.qrcodeview.*
import java.util.*


/**
 * This Activity is exactly the same as CaptureActivity, but has a different orientation
 * setting in AndroidManifest.xml.
 */
class ScannerActivity : Activity(), DecoratedBarcodeView.TorchListener {

    var switchFlashlightButtonCheck: Boolean = true
    var capture: CaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        if (!hasFlash()) {
            btn_flashlight.visibility = View.GONE
        }

        qr_barcode.setTorchListener(this)
        capture = CaptureManager(this, qr_barcode)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.decode()

        switchFlashlight()
    }

    fun hasFlash (): Boolean {
        return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun switchFlashlight() {
        btn_flashlight.setOnClickListener {
            if (switchFlashlightButtonCheck) {
                qr_barcode.setTorchOn()
            } else {
                qr_barcode.setTorchOff()
            }
        }
    }

    fun changeMaskColor() {
        val rnd = Random()
        val color: Int = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
//        zxing_viewfinder_vie(color)
    }

    fun DisableLaser() {
        }



    override fun onTorchOn() {
        switchFlashlightButtonCheck = false
    }

    override fun onTorchOff() {
        switchFlashlightButtonCheck = true //켜기
    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
        capture?.onSaveInstanceState(outState)
    }

}