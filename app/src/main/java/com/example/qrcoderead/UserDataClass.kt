package com.example.qrcoderead

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@Serializable
 data class UserDataClass(
    var name: String = "",        //name
    var hp: String  = "",         // number
    var data: String = "",        // memo
    var dstamp: String = "",      //Qrcode read
    val key: String = "$2b$12\$RlTB6LYhc4ahhVJTW0t.E.eoGlsa7wEzijZXR27kgq5r2/FJ2FW5G" //API Key
)