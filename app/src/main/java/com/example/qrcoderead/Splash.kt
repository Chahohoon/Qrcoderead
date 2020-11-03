package com.example.qrcoderead

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.delete

class Splash : AppCompatActivity() {

    var userdata = UserDataClass()
    var userload = UserDataLoadClass()
    var realm : Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        onInitDataBase()
        checkUserdata()
    }

    fun onInitDataBase(){

        Realm.init(this)
        var config = RealmConfiguration.Builder().name("myrealm.realm").build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }


    fun checkUserdata() {
        val intentMain = Intent(this, MainActivity::class.java)
        val intentUserInfo = Intent(this, UserInfo::class.java)

        // 내부 디비가 존재하지 않으면 신규 작성?
        val localdb = realm?.where(UserDataLoadClass::class.java)?.findAll()
        //초기값 0 , 값이 들어오면 1
        if (localdb?.size == 0){
            startActivity(intentUserInfo)
            finish()
        } else {
            startActivity(intentMain)
            finish()
        }



        //        FirebaseDatabase.getInstance().reference?.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {}
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userdata = snapshot.getValue<UserDataClass>()!!
//                Log.d("파이어 베이스 읽어오기", userdata.toString())
//                // 파이어베이스 데이터 수신 이후 작업
//            }
//        })

//        usdatabase?.reference?.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (snapshot in snapshot.children) {
//                    var username = snapshot.key.toString()
//                    //firebase에 저장된 key값을 담는 배열
////                    userdata.userlist.add(username)
////                    Log.d("realm 저장완료4", userdata.userlist.toString())
//                }
//
//            }
//        })
    }
}