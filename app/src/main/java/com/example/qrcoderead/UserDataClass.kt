package com.example.qrcoderead

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
 data class UserDataClass(
    var name: String = "",        //name
    var hp: String  = "",         // number
    var data: String = "",        // memo
    var dstamp: String = "",      //Qrcode read
    var key: String = ""          //API Key
)