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

data class infolist(
    val data: String,
    val name: String,
    val hp: String
)

data class visitlist(
    val data: String,
    val div: String,
    val gps: String,
    val site: String,
    val time: String
)

enum class InfoItem(name: String) {
    방명록("data"),
    이름("name"),
    번호("hp"),
    가게명("div"),
    위치("gps"),
    사이트("site"),
    시간("time")
}

class UserCoreClass {

    var userdata = UserDataClass()
    var userqrcodelist = UserQrcodeList()
    val okHttpClient = OkHttpClient()
    var realm : Realm? = null

    var infoList = mutableMapOf<String, String>()
    var visitList = mutableMapOf<String, String>()
    val testvv = mutableListOf<String>()

    fun JSONArray.visitList(): MutableList<JSONObject> = MutableList(length(), this::getJSONObject)

    //유저 정보 서버에 등록
    fun setUserDataPost(name: String, hp: String, data: String, dstamp: String) {
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

    // 누가 왔다갔는지 확인
    fun getUserDataList(name: String, hp: String, data: String, dstamp: String) {
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
//
                            var res = userdata.getJSONObject(index)

//                            DataListt = res as MutableMap<String, String>

                            //  [{"data":"방문","hp":"010-9323-****","name":"차호*"}
                        }
                        infoList = res as MutableMap<String, String>

                        res.keys
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.d("DataListt3", res.toString())
                    Log.d("DataListt3", "$infoList")
                    Log.d("DataListt4", "${infoList}")
                    Log.d("DataListt5", infoList.toString())
                    Log.d("DataListt6", infoList.values.toString())
                    Log.d("DataListt7", infoList.keys.toString())
                    //{"data":"\ubc29\ubb38","hp":"010-9323-****","name":"\ucc28\ud638*"}
                }
            }

        })
    }

    //방문했던 곳 확인
    fun getUserVisitList(name: String, hp: String, data: String, dstamp: String) {
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
                    try {
                        val visitobj = JSONObject(res)
                        val visitdata = visitobj.getJSONArray("Value")

                        visitList
//
//                        Log.d("getUserVisitList2", "$res")
                        // data : , div : , gps : , site : , time :
                        //{"data":"\ubc29\ubb38","div":"beauty","gps":"37.364809,127.107684","site":"THE_NINE","time":"20-10-29 10:37"}

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    System.out.println(visitList)
//                    Log.d("getUserVisitList", "$res")
                }
            }

        })
    }

    fun getData(info: InfoItem): String {
        return when(info) {
            InfoItem.방명록 -> {
                infoList[InfoItem.방명록.name].toString()
            }
            InfoItem.이름 -> {
                infoList[InfoItem.이름.name].toString()
            }
            InfoItem.번호 -> {
                infoList[InfoItem.번호.name].toString()
            }
            InfoItem.가게명 -> {
                visitList[InfoItem.가게명.name].toString()
            }
            InfoItem.사이트 -> {
                visitList[InfoItem.사이트.name].toString()
            }
            InfoItem.위치 -> {
                visitList[InfoItem.위치.name].toString()
            }
            InfoItem.시간 -> {
                visitList[InfoItem.시간.name].toString()
            }

            else -> {""}
        }
    }

}