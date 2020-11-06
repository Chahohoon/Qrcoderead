package com.example.qrcoderead

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.userqrcodelist.*
import kotlinx.android.synthetic.main.userqrcodelist_item.*
import kotlinx.android.synthetic.main.userqrcodelist_item.view.*

class UserQrcodeList : Activity() {

    var Adapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userqrcodelist)


        qrcodelist_view.adapter = Adapter

    }

}

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var items = mutableListOf<UserDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("Not yet implemented")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.userqrcodelist_item,parent,false)
        return ViewHolder(view)
    }

    ///아이템 리스트화
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}

