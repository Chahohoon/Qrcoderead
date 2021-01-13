package com.example.qrcoderead

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.uservisitlist.*

class UserVisitList : AppCompatActivity() {
    val adapter = VisitAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uservisitlist)

        visitlist_view.adapter = adapter

    }


}
