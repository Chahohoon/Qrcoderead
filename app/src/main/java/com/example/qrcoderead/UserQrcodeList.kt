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
import kotlinx.android.synthetic.main.userqrcodelist_item.*

class UserQrcodeList : AppCompatActivity() {

    var Adapter = ListAdapter()
    var usercoreclass = UserCoreClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userqrcodelist)


    }

    fun getQrcodeList(data : String, hp : String, name : String) {

    }

}

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var userqrcodelist = UserQrcodeList()
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

        holder.tv_name2.text = userqrcodelist.getQrcodeList()
        holder.tv_hp2.text
        holder.tv_memo2.text

    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}

