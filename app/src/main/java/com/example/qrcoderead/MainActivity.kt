package com.example.qrcoderead

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
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
        setMemo()

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
           realm?.where(UserDataLoadClass::class.java)?.findFirst()?.let {
                   userdata.name = it.isName()
                   userdata.hp = it.isnumber()
           }
        }
    }

    fun ReadQrcode() {
        integrator.setCaptureActivity(ScannerActivity::class.java)
        integrator.initiateScan()
        integrator.setBeepEnabled(true) //인식 시 "삑" 소리 남
    }

    fun setMemo() {
        ed_memo.addTextChangedListener(object : TextWatcher {
            //입력하기 전에
            override fun afterTextChanged(p0: Editable?) {
                userdata.data = p0.toString()
            }

            //입력이 끝날때
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ed_memo.clearFocus()
            }
        })

        if(userdata.data == "") {
            userdata.data = "방문"
        }
    }

    fun getAPi() {
        val url =""
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        //동기 : execute // 비동기 : enqueue
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
//                var address = JSONArray(JSONObject(res))
            }
        })

    }


    fun postAPi() {
        onReadDataBase()

        val url = "https://www.dstamp.kr/api/v1/usersite"
        val mediaType = "application/json;charset=utf-8".toMediaType()
        val json = Gson().toJson(userdata)
        val requestBody = json.toString().toRequestBody(mediaType)
        Log.d("QRcode", "$requestBody")
        //디버깅할때
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                Log.d("QRcode", response.isSuccessful.toString())
                if (!response.isSuccessful) {
                    Log.d("QRcode3", "응답실패")
                } else {
//                    var userdata = JSONArray(JSONObject(res).getString("name"))

                    Log.d("QRcode", "$res")
                    Log.d("QRcode", "응답성공")


//                    runOnUiThread(object : Runnable {
//                        override fun run() {
//                            try {
////                                val userdata = JSONObject(res)
////                                val parentJArray = userdata.getString("responseCode")
//
//                                var userdata = JSONArray(JSONObject(res).getString("data"))
//                                Log.d("QRcode", "$userdata")
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                            }
//                        }
//                    })
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

    override fun onResume() {
        super.onResume()
        ed_memo.text?.clear()
        ed_memo.clearFocus()
        setMemo()
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
                postAPi()
//                FirebaseDataWrite()
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

    fun jsonKeyValue() {
        val jsonString = """
        {
            "name": "${userdata.name}",
            "hp": "${userdata.hp}",
            "data": "${userdata.data}",
            "dstamp": "${userdata.dstamp}",
            "key": "${userdata.key}"
        }
    """.trimIndent()
        //trimIndent 들여쓰기 제거

        val jObject: JSONObject = JSONObject(jsonString)

        val name = jObject.getString("title")
        val hp = jObject.getString("url")
        val data = jObject.getString("data")
        val dstamp = jObject.getString("dstamp")
        val key = jObject.getString("key")

    }

//    interface RetrofitAPI {
//        @FormUrlEncoded
//        @POST("https://www.dstamp.kr/api/v1/userstamp/posts")
//        fun keyValue (@Field("name") name : String,
//                      @Field("hp") hp : String,
//                      @Field("data") data : String,
//                      @Field("dstamp") dstamp : String,
//                      @Field("key") key : String
//                      ): Call<>
//    }
}

