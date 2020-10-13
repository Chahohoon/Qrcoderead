package com.example.qrcoderead

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.userinfo.*

class UserInfo : AppCompatActivity() {

    var userdata = UserDataClass()
    var realm : Realm? = null
    var toast : Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userinfo)

        onInitDataBase()
        setUserData()

        btn_save.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if(ed_name.text.toString().equals("") && ed_number.text.toString().equals("") ) {
                toast = Toast.makeText(this,"정보를 입력해 주세요",Toast.LENGTH_SHORT)
                toast?.show()
            }else {
                setUserinfo(userdata.curName,userdata.curNumber)
                startActivity(intent)
                finish()
            }
        }
    }

    //realm 초기화
    fun onInitDataBase(){
        Realm.init(this)
        var config = RealmConfiguration.Builder().name("myrealm.realm").build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }

    //edittext 처리
    fun setUserData() {

        ed_name.addTextChangedListener(object : TextWatcher {
            //입력하기 전에
            override fun afterTextChanged(p0: Editable?) {
            }

            //입력이 끝날때
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ed_name.setBackgroundResource(R.drawable.background_edittext)
                userdata.curName = p0.toString()
            }

            //타이핑 중에
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ed_name.setBackgroundResource(R.drawable.error_edittext)
            }

        })

        ed_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ed_name.setBackgroundResource(R.drawable.background_edittext)
                userdata.curNumber = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ed_name.setBackgroundResource(R.drawable.error_edittext)
            }

        })


    }

    
    
    //Firebase, realm Database 처리
    fun setUserinfo(name : String, number : String) {
        var derf = userdata.dref
        var curUserdata = mutableMapOf<String,String>()
        curUserdata.put("UserName",userdata.curName)
        curUserdata.put("UserNumber",userdata.curNumber)
        derf?.child(userdata.curName)?.setValue(curUserdata)
        Log.d("파이어베이스 저장완료", curUserdata.toString())

        realm?.executeTransaction {
            val check = realm?.where(UserDataLoadClass::class.java)?.equalTo("name",name)?.findFirst()
            if(check == null) {
                val temp = it.createObject(UserDataLoadClass::class.java)
                temp.setData(name,number)
                Log.d("realm 저장완료", temp.toString())
            }
        }

    }
}