package com.example.qrcoderead

import io.realm.RealmObject


open class UserDataLoadClass : RealmObject() {
    private var name : String? = null
    private var number : String? = null
//    private var usermemo : String? = null

    fun setData(name : String, number : String){
        this.name = name
        this.number = number
//        this.usermemo = usermemo
    }

    fun isName() : String {
        return this.name ?: ""
    }

    fun isnumber() : String{
        return this.number ?: ""
    }

//    fun isusermemo() : String{
//        return this.usermemo ?: ""
//    }

}