package com.example.qrcoderead

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.userdialog.*
import okhttp3.OkHttpClient
import okhttp3.Response

class MainActivity : AppCompatActivity() {


    private lateinit var detector : BarcodeDetector
    private lateinit var cameraSource: CameraSource

    var realm : Realm? = null
    var userdata = UserDataClass()
    var QRcodereading = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ReadQrcode()

        if(userdata.readvalue != ("")) {
            memocheck(userdata.readvalue)
        }

    }

    //realm 초기화
    fun onInitDataBase(){
        Realm.init(this)
        var config = RealmConfiguration.Builder().name("myrealm.realm").build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }


    //카메라 권한 퍼미션 체크
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 200){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                cameraSource.start(qr_barcode.holder)
            } else {
                Toast.makeText(this, "스캐너는 카메라 권한이 허용되어야 사용 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun ReadQrcode() {
        //바코드 설정
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_QR_CODE
            ).build()

        

        //카메라 설정
//        cameraSource = CameraSource.Builder(this,detector).setRequestedPreviewSize(1024,768)
//            .setRequestedFps(25f).setAutoFocusEnabled(true).build()
//
//        qr_barcode.holder.addCallback(object : SurfaceHolder.Callback2 {
//            override fun surfaceRedrawNeeded(p0: SurfaceHolder) {
//
//            }
//
//            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//
//            override fun surfaceDestroyed(p0: SurfaceHolder) {
//                cameraSource.stop()
//            }
//
//            override fun surfaceCreated(p0: SurfaceHolder) {
//                if(ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                    cameraSource.start(p0)
//                } else {
//                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.CAMERA), 200)
//                }
//            }
//
//        })
//
//        //결과 디스플레이
//        detector.setProcessor(object : Detector.Processor<Barcode> {
//            override fun release() {
//            }
//
//            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
//                var barcodes = p0?.detectedItems
//                if (barcodes!!.size() > 0) {
//                    val Stringresult = StringBuilder()
//                    //결과값 출력
//                    resultext.post {
//                        Stringresult.append("결과 :")
//                        Stringresult.append("\n")
//                        Stringresult.append(barcodes.valueAt(0).displayValue)
//                        resultext.text = Stringresult.toString()
//                        userdata.readvalue = Stringresult.toString()
//
////                        var curUserdata = mutableMapOf<String,String>()
////                        curUserdata.put("Qrcode",userdata.readvalue)
////                        userdata.dref?.child(userdata.curName)?.setValue(curUserdata)
//                    }
//                }
//            }
//        })


    }

    fun apicheck() {
        val okHttpClient = OkHttpClient
        val url =""
        val request = ""

    }

    fun memocheck(readvalue : String) {
        val dig = UserDialog(this)

        if(readvalue != "")
            dig.start("현재 위치에 대한 메모를 남기시겠습니까?")
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
    }


}

