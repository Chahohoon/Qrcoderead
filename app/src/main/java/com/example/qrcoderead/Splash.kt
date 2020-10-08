package com.example.qrcoderead

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.userinfo.*
import java.util.ArrayList

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


    //fierbase 데이터 받아오는곳 
    fun checkUserdata() {
        val intentMain = Intent(this@Splash, MainActivity::class.java)
        val intentUserInfo = Intent(this@Splash, UserInfo::class.java)
        val check :RealmResults<UserDataLoadClass>? = realm?.where(UserDataLoadClass::class.java)?.findAll()

        userdata.database = FirebaseDatabase.getInstance()

        userdata.database?.getReference(userdata.curName)?.addListenerForSingleValueEvent(object : ValueEventListener {
            var username = ""
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    username = snapshot.key.toString()
                    userdata.userlist.add(username)
                }

            }
        })

        System.out.println(check)
        if(!userdata.userlist.equals(check)) {
            startActivity(intentMain)
            finish()
        } else {
            startActivity(intentUserInfo)
            finish()
        }
    }
}