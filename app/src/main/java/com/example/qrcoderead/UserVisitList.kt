package com.example.qrcoderead

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.uservisitlist.*

class UserVisitList : AppCompatActivity() {

    val adapter = VisitAdapter()
    var datainfo = mutableListOf<UserDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uservisitlist)

        adapter.replaceItems(datainfo)
        visitlist_view.adapter = adapter
    }


}
