package com.example.qrcoderead

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


 class UserDataClass {
    var curName = ""
    var curNumber  = ""
    var curMemo  = ""
    var readvalue = ""
    var database : FirebaseDatabase? = null
    var dref : DatabaseReference? =  null
    var userlist = mutableListOf<String>()
    var userlistrealm =  mutableListOf<String>()

}