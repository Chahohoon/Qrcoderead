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
    private var usercheck = listOf<String>()

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


    //fierbase date read
    fun checkUserdata() {
        val check = realm?.where(UserDataLoadClass::class.java)?.findAll()
        var usdatabase = userdata.database
        usdatabase = FirebaseDatabase.getInstance()

        usdatabase?.reference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    var username = snapshot.key.toString()
                    //firebase에 저장된 key값을 담는 배열
                    userdata.userlist.add(username)
                    Log.d("realm 저장완료4", userdata.userlist.toString())
                }

            }
        })
        Log.d("체크엔 뭐가있냐 ", check.toString())
        getUserData()
    }

    fun getUserData() {
        val intentMain = Intent(this@Splash, MainActivity::class.java)
        val intentUserInfo = Intent(this@Splash, UserInfo::class.java)
        var userretry = listOf<String>()
        realm?.executeTransaction {
            val check = realm?.where(UserDataLoadClass::class.java)?.findAll()
            check?.let {

                for (i in it) {
                    UserDataLoadClass()?.let {
                        i.isName()
                        i.isnumber()
                    }
                    userretry = listOf(i.toString())
                    Log.d("내부DE 읽음", i.toString())
                }
            }
        }
        //realm 저장은 되나
        Log.d("c체크1", userretry.toString())
        Log.d("체크2", usercheck.toString())
        if (userretry.size == 0  ) {
            startActivity(intentUserInfo)
            finish()
        } else {
                startActivity(intentMain)
                finish()
                //비어있으면
        }
    }
}