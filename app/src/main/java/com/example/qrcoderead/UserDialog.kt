package com.example.qrcoderead

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView


class UserDialog(context: Context) {
    var dig = Dialog(context)
    var userdata = UserDataClass()
    var activity : MainActivity = MainActivity()
    private lateinit var usermemo : TextView
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button

    fun start(content : String) {
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀 바 제거
        dig.setContentView(R.layout.userdialog) //다이얼로그 띄움
        dig.setCancelable(false) //다이얼로그의 바깥 부분 클릭 시 안닫히게

        usermemo = dig.findViewById(R.id.content)
        usermemo.text = content

        btnOK = dig.findViewById(R.id.ok)
        btnOK.setOnClickListener{
            dig.dismiss() //다이얼로그 종료 메소드
        }

        btnCancel = dig.findViewById(R.id.cancel)
        btnCancel.setOnClickListener{
            userdata.curMemo = "방문"
            dig.dismiss() //다이얼로그 종료 메소드
        }

        dig.show()
    }

}