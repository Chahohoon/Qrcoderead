package com.example.qrcoderead

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
 data class UserDataClass(
    var curName: String = "",
    var curNumber: String  = "",
    var curMemo: String = "",
    var readvalue: String = ""
)