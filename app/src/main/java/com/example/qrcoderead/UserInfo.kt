package com.example.qrcoderead

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.userinfo.*

class UserInfo : AppCompatActivity() {

    var userdata = UserDataClass()
    var toast : Toast? = null
    var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userinfo)

        checkUserdata()
        setUserData()

        btn_save.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if(ed_name.text.toString().equals("") && ed_number.text.toString().equals("") ) {
                toast = Toast.makeText(this,"정보를 입력해 주세요",Toast.LENGTH_SHORT)
                toast?.show()
            }else if (username.equals(userdata.curName)) {
                startActivity(intent)
                finish()
            }else {
                setUserinfo()
                startActivity(intent)
                finish()
            }
        }
    }

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

    fun setUserinfo() {
        var curUserdata = mutableMapOf<String,String>()
        curUserdata.put("UserName",userdata.curName)
        curUserdata.put("UserNumber",userdata.curNumber)
        userdata.dref?.child(userdata.curName)?.setValue(curUserdata)
    }

    fun checkUserdata() {
        userdata.database?.reference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    username = snapshot.key.toString()
                }

                }
        })
    }
}