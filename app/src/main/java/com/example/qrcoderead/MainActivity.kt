package com.example.qrcoderead

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {


    private lateinit var detector : BarcodeDetector
    private lateinit var cameraSource: CameraSource
    val integrator = IntentIntegrator(this)

    var realm : Realm? = null
    var userdata = UserDataClass()
    var userdataload = UserDataLoadClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentMain = Intent(this, ScannerActivity::class.java)
        onInitDataBase()
//        ReadQrcode()

        btn_qrcode.setOnClickListener {
            ReadQrcode()
        }


    }

    //realm 초기화
    fun onInitDataBase(){
        Realm.init(this)
        var config = RealmConfiguration.Builder().name("myrealm.realm").build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

    }

    // realm 읽어오기
    fun onReadDataBase() {
        realm?.executeTransaction {
            var datas = realm?.where(UserDataLoadClass::class.java)?.findAll()
            var copydatas = realm?.copyToRealm(datas)
            datas?.let{
                for (i in it){
                    userdata.curName = i.isName()
                    userdata.curNumber = i.isnumber()
                    userdata.curMemo = i.isusermemo()
                }
            }
        }
    }

    fun ReadQrcode() {
//        //결과 디스플레이
//        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()
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
//                        detector.release()
//                        cameraSource.stop()
//
//                        if (userdata.readvalue != "") {
////                            memocheck()
//                            FirebaseDataWrite()
//                        }
//
//                    }
//                }
//            }
//        })
//
//        //카메라 설정
//        cameraSource = CameraSource.Builder(this, detector).setRequestedPreviewSize(1024, 768)
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
//                if (ContextCompat.checkSelfPermission(
//                        this@MainActivity,
//                        android.Manifest.permission.CAMERA
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    cameraSource.start(p0)
//                } else {
//                    ActivityCompat.requestPermissions(
//                        this@MainActivity,
//                        arrayOf(android.Manifest.permission.CAMERA),
//                        200
//                    )
//                }
//            }
//
//        })
//
    integrator.setCaptureActivity(ScannerActivity::class.java)
    integrator.setBeepEnabled(true) //인식 시 "삑" 소리 남
    integrator.initiateScan()
    }


    fun apicheck() {
        val okHttpClient = OkHttpClient
        val url =""
        val request = ""

    }

    fun FirebaseDataWrite() {
        onReadDataBase()
        // 파이어 베이스 저장
        FirebaseDatabase.getInstance().reference.child(userdata.curName).setValue(userdata)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                userdata.readvalue = result.contents
                FirebaseDataWrite()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    
    //activity가 화면에서 사라질때
    override fun onDestroy() {
        super.onDestroy()
    }

    //카메라 권한 퍼미션 체크
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 200){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                cameraSource.start(qr_barcode.holder)
            } else {
                Toast.makeText(this, "스캐너는 카메라 권한이 허용되어야 사용 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

