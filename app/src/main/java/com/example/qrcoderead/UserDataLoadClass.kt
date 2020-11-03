package com.example.qrcoderead

import io.realm.RealmObject


open class UserDataLoadClass : RealmObject() {
    private var name : String? = ""
    private var number : String? = ""
    private var memo : String? = ""

    fun setData(name : String, number : String, memo : String){
        this.name = name
        this.number = number
        this.memo = memo
    }

    fun isName() : String {
        return this.name ?: ""
    }

    fun isnumber() : String{
        return this.number ?: ""
    }

    fun isMemo() : String {
        return this.memo ?: ""
    }


}