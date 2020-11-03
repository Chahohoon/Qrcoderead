package com.example.qrcoderead

import android.util.Log
import com.google.gson.Gson
import io.realm.Realm
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class UserCoreClass {

    val userdata = UserDataClass()
    val okHttpClient = OkHttpClient()
    var realm : Realm? = null

    fun onReadDataBase() {
        realm?.executeTransaction {
            realm?.where(UserDataLoadClass::class.java)?.findFirst()?.let {
                userdata.name = it.isName()
                userdata.hp = it.isnumber()
                userdata.data = it.isMemo()
            }
        }
    }

    fun setUserDataPost() {
        //내부 DB에 저장된 값 userdata 클래스에 넣기
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
                }
            }

        })

    }

    fun getUserDataList() {

    }

    fun getUserVisitList() {

    }
}