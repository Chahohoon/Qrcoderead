package com.example.qrcoderead

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class MomoDialog(context: Context) {
    var dig = Dialog(context)
    var userdata = UserDataClass()
    private lateinit var usermemo : TextView
    private lateinit var curmemo : EditText
    private lateinit var btnOK : Button

    fun start(content : String) {
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀 바 제거
        dig.setContentView(R.layout.userdialog) //다이얼로그 띄움
        dig.setCancelable(false) //다이얼로그의 바깥 부분 클릭 시 안닫히게

        usermemo = dig.findViewById(R.id.content)
        usermemo.text = content

        btnOK = dig.findViewById(R.id.ok)
        btnOK.setOnClickListener{
            setUserData()
            dig.dismiss() //다이얼로그 종료 메소드
        }


        dig.show()
    }

    fun setUserData() {
        curmemo = dig.findViewById(R.id.ed_memo)

        curmemo.addTextChangedListener(object : TextWatcher {
            //입력하기 전에
            override fun afterTextChanged(p0: Editable?) {
            }

            //입력이 끝날때
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                curmemo.setBackgroundResource(R.drawable.background_edittext)
                userdata.curName = p0.toString()
            }

            //타이핑 중에
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                curmemo.setBackgroundResource(R.drawable.error_edittext)
            }

        })

        curmemo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                curmemo.setBackgroundResource(R.drawable.background_edittext)
                userdata.curMemo = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                curmemo.setBackgroundResource(R.drawable.error_edittext)
            }

        })


    }

}