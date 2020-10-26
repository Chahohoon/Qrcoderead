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
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {


    private lateinit var detector : BarcodeDetector
    private lateinit var cameraSource: CameraSource
    val integrator = IntentIntegrator(this)
    val okHttpClient = OkHttpClient()
    var realm : Realm? = null
    var userdata = UserDataClass()
    var userdataload = UserDataLoadClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onInitDataBase()

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
            datas?.let{
                for (i in it){
                    userdata.name = i.isName()
                    userdata.hp = i.isnumber()
                    userdata.data = i.isusermemo()
                }
            }
        }
    }

    fun ReadQrcode() {
        integrator.setCaptureActivity(ScannerActivity::class.java)
        integrator.initiateScan()
        integrator.setBeepEnabled(true) //인식 시 "삑" 소리 남
    }



    fun getAPi() {
        val url =""
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .addHeader("","")
            .url(url)
            .build()

        //동기 : execute // 비동기 : enqueue
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                var address = JSONArray(JSONObject(res).getString(""))
            }
        })

    }


    fun postAPi() {
        val url = ""
        val okHttpClient = OkHttpClient()

        var requestbody = FormBody.Builder()
            .add("name", userdata.name)
            .add("hp",  userdata.hp)
            .add("data", userdata.data)
            .add("dstam", userdata.dstamp)
            .add("key", userdata.key)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestbody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
//                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
//                TODO("Not yet implemented")
                val res = response.body?.string()
                try {

                } catch (e:JSONException) {
                    e.printStackTrace()
                }
            }

        })
    }

    fun FirebaseDataWrite() {
        //realm 데이터 불러오기
        onReadDataBase()
        // 파이어 베이스 저장
        FirebaseDatabase.getInstance().reference.child(userdata.name).setValue(userdata)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                userdata.dstamp = result.contents
                FirebaseDataWrite()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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

