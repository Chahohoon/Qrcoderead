package com.example.qrcoderead

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.autofill.UserData
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scanner.*
import kotlinx.android.synthetic.main.userinfo.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringFromUtf8Bytes
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Utf8
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.io.IOException
import java.util.*


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
        userdata.data = "방문"

        val url = "https://www.dstamp.kr/api/v1/userstamp"
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val json = Gson().toJson(userdata)
        val requestBody = json.toString().toRequestBody(mediaType)
        val okHttpClient = OkHttpClient()

        //디버깅할때
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        Log.d("DDDD", requestBody.toString())
        okHttpClient.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                try {
                    var address = JSONArray(JSONObject(res))
                    Log.d("DDDD", address.toString())
//                    System.out.println(address)
                } catch (e:JSONException) {
                    e.printStackTrace()
                }
            }

        })
//        System.out.println(requestBody)
//        var retrofit = Retrofit.Builder()
//            .baseUrl(url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()


    }

    fun FirebaseDataWrite() {
        //realm 데이터 불러오기
        onReadDataBase()
        userdata.data = "방문"
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
//                setMemo()
                postAPi()
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

