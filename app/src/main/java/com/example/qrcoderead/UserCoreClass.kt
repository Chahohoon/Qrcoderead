package com.example.qrcoderead

import android.util.Log
import com.google.gson.Gson
import io.realm.Realm
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

data class DataList (
    var data : String = "",
    var hp : String = "",
    var name : String = ""
)

class UserCoreClass {

    var userdata = UserDataClass()
    var userqrcodelist = UserQrcodeList()
    val okHttpClient = OkHttpClient()
    var realm : Realm? = null

    var DataList = mutableListOf<DataList>()


    fun setUserDataPost(name : String, hp : String, data : String, dstamp : String) {
        //내부 DB에 저장된 값 userdata 클래스에 넣기

        userdata.name = name
        userdata.hp = hp
        userdata.data = data
        userdata.dstamp = dstamp

        val url = "https://www.dstamp.kr/api/v1/userstamp"
        val mediaType = "application/json;charset=utf-8".toMediaType()
        val json = Gson().toJson(userdata)
        val requestBody = json.toString().toRequestBody(mediaType)
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
                if (!response.isSuccessful) {
                    Log.d("setUserDataPost", "응답실패")
                } else {
//                    var userdata = JSONArray(JSONObject(res).getString("name"))

                    Log.d("뭘까1", "$res")
                    Log.d("setUserDataPost", "응답성공")
                }
            }

        })

    }

    fun getUserDataList(name : String, hp : String, data : String, dstamp : String) {
        userdata.name = name
        userdata.hp = hp
        userdata.data = data
        userdata.dstamp = dstamp

        val url = "https://www.dstamp.kr/api/v1/sitestamp"
        val mediaType = "application/json;charset=utf-8".toMediaType()
        val json = Gson().toJson(userdata)
        val requestBody = json.toString().toRequestBody(mediaType)
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
                if (!response.isSuccessful) {
                    Log.d("getUserDataList", "응답실패")
                } else {
                    try {

                        var userdata = JSONArray(JSONObject(res))

                        //data: , hp : , name : 모든 유저 불러옴
                        // 받은걸 여기서 다시 액티비티로 넘겨야 함
                        for (index in 0 until userdata.length()) {

                            DataList = mutableListOf<DataList>()
                            var res = userdata.getJSONObject(index)
                            var data = res.get("data").toString()
                            var hp = res.get("hp").toString()
                            var name = res.get("name").toString()

                            userqrcodelist.getQrcodeList(data, hp, name)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.d("getUserDataList", "응답성공")
                }
            }

        })
    }

    fun getUserVisitList(name : String, hp : String, data : String, dstamp : String) {
        userdata.name = name
        userdata.hp = hp
        userdata.data = data
        userdata.dstamp = dstamp

        val url = "https://www.dstamp.kr/api/v1/usersite"
        val mediaType = "application/json;charset=utf-8".toMediaType()
        val json = Gson().toJson(userdata)
        val requestBody = json.toString().toRequestBody(mediaType)
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
                if (!response.isSuccessful) {
                    Log.d("getUserVisitList", "응답실패")
                } else {
//                    var userdata = JSONArray(JSONObject(res).getString("name"))

                    Log.d("뭘까3", "$res")


                    // data : , div : , gps : , site : , time :
                    Log.d("getUserVisitList", "응답성공")
                }
            }

        })
    }
}