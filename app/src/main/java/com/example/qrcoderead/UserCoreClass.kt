package com.example.qrcoderead

import android.util.Log
import com.google.gson.Gson
import kotlinx.serialization.Serializable
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

class UserCoreClass {

    var userdata = UserData()
    var userqrcodelist = UserQrcodeList()


}


