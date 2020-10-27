package com.example.qrcoderead

import io.realm.RealmObject


open class UserDataLoadClass : RealmObject() {
    private var name : String? = ""
    private var number : String? = ""

    fun setData(name : String, number : String){
        this.name = name
        this.number = number
    }

    fun isName() : String {
        return this.name ?: ""
    }

    fun isnumber() : String{
        return this.number ?: ""
    }


}