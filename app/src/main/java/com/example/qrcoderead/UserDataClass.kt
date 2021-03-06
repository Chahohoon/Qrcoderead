package com.example.qrcoderead

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

data class UserData(
    var name: String = "",        //name
    var hp: String  = "",         // number
    var data: String = "",        // memo
    var dstamp: String = "",      //Qrcode read
    val key: String = "$2b$12\$RlTB6LYhc4ahhVJTW0t.E.eoGlsa7wEzijZXR27kgq5r2/FJ2FW5G"
)

enum class InfoItem(name: String) {
   방명록("방명록"),
   이름("이름"),
   번호("번호"),
   가게명("가게명"),
   위치("위치"),
   사이트("사이트"),
   시간("시간")
}

class UserDataClass {

   val key: String = "$2b$12\$RlTB6LYhc4ahhVJTW0t.E.eoGlsa7wEzijZXR27kgq5r2/FJ2FW5G"
   var infoList = mutableMapOf<String, String>()
   var visitList = mutableMapOf<String, String>()

   //유저 정보 서버에 등록
   fun setUserDataPost(name: String, hp: String, data: String, dstamp: String) {
      //내부 DB에 저장된 값 userdata 클래스에 넣기

      var userdata = UserData(name,hp,data,dstamp)


      val url = "https://www.dstamp.kr/api/v1/userstamp"
      val okHttpClient = OkHttpClient()
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

      var userdata = UserData(name,hp,data,dstamp)
      val okHttpClient = OkHttpClient()

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

      var userdata = UserData(name,hp,data,dstamp)

      val url = "https://www.dstamp.kr/api/v1/usersite"
      val okHttpClient = OkHttpClient()
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
         }

         override fun onResponse(call: Call, response: Response) {
            val res = response.body?.string()
            if (!response.isSuccessful) {
               Log.d("getUserVisitList", "응답실패")
            } else {
               try {
                  val visitobj = JSONArray(res)

                  for (i in 0 until visitobj.length()) {
                     val result = visitobj.getJSONObject(i)
                     Log.d("TEST", "$result")


                     var sdata = result.getString("data")
                     var sdiv = result.getString("div")
                     var sgps = result.getString("gps")
                     var ssite = result.getString("site")
                     var stime = result.getString("time")

                     visitList.put("방명록", sdata)
                     visitList.put("가게명", sdiv)
                     visitList.put("위치", sgps)
                     visitList.put("사이트", ssite)
                     visitList.put("시간", stime)

                     setData(visitList)


                  }
                  Log.d("TEST2.1", "$visitobj")
                  Log.d("TEST2", "$visitList")


               } catch (e: JSONException) {
                  e.printStackTrace()
               }
            }
         }
      })
   }

   fun setData(ABC:MutableMap<String,String>) {
      if(ABC.size != 0) {
         visitList = ABC  //ABC의 values 값을 mutablemap 값으로 자료형 변환
      }
      Log.d("TEST3", "$visitList")
      Log.d("TEST3.5", InfoItem.가게명.name)
   }

   fun getData(info: InfoItem): String {
      Log.d("TEST4", "$visitList")
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
      }

   }
}

