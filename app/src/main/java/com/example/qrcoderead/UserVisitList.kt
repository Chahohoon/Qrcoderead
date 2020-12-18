package com.example.qrcoderead

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.uservisitelist_item.*
import kotlinx.android.synthetic.main.uservisitelist_item.view.*
import kotlinx.android.synthetic.main.uservisitlist.*

class UserVisitList : AppCompatActivity() {

    var adapter = VisitAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uservisitlist)

        visitlist_view.adapter = adapter
        Log.d("time3", "시간").toString()
    }


    override fun onResume() {
        super.onResume()
        visitlist_view.adapter = adapter
    }
}

class VisitAdapter : RecyclerView.Adapter<VisitAdapter.ViewHolder>() {

    private var items = mutableListOf<UserCoreClass>()

    //화면을 최초 로딩하여 만들어진 view가 없을경우 레이아웃을 inflate함
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("Not yet implemented")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.uservisitelist_item,parent,false)
        return ViewHolder(view)
    }

    ///레이아웃의 view와 데이터를 연결 
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VisitAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        Log.d("time2", "시간").toString()
        holder.tv_test.text = item.getData(InfoItem.시간)

    }

    fun replaceItems(items: MutableList<UserCoreClass>) {
        this.items = items
        notifyDataSetChanged()
    }

    //아이템 갯수 리턴처리
    override fun getItemCount(): Int = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}