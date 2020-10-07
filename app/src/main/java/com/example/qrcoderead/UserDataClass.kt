package com.example.qrcoderead

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserDataClass {
    var curName = ""
    var curNumber  = ""
    var curMemo  = ""
    var readvalue = ""
    var database : FirebaseDatabase? = FirebaseDatabase.getInstance()
    var dref : DatabaseReference? = database?.getReference()
    var userlist = mutableListOf<String>()

}