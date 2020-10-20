package com.example.qrcoderead

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.android.synthetic.main.activity_scanner.*


/**
 * This Activity is exactly the same as CaptureActivity, but has a different orientation
 * setting in AndroidManifest.xml.
 */
class ScannerActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {

    var  switchFlashlightButtonCheck : Boolean = true
    var capture : CaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        qr_barcode.setTorchListener(this)
        capture = CaptureManager(this, qr_barcode)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.decode()

    }


    override fun onTorchOn() {
        switchFlashlightButtonCheck = true
    }

    override fun onTorchOff() {
        switchFlashlightButtonCheck = false
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

    fun switchFlashlight(view: View?) {
        if (switchFlashlightButtonCheck) {
            qr_barcode.setTorchOn()
        } else {
            qr_barcode.setTorchOff()
        }
    }
}