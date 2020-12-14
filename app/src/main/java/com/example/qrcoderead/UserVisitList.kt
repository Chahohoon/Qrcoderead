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
import kotlinx.android.synthetic.main.uservisitlist.*

class UserVisitList : AppCompatActivity() {
    var adapter = VisitAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uservisitlist)

        visitlist_view.adapter = adapter
    }

}

class VisitAdapter : RecyclerView.Adapter<VisitAdapter.ViewHolder>() {

    private var items = mutableListOf<UserCoreClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("Not yet implemented")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.uservisitelist_item,parent,false)
        return ViewHolder(view)
    }

    ///아이템 리스트화
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VisitAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        holder.tv_test.text = item.getData(InfoItem.시간)

    }

    fun replaceItems(items: MutableList<UserCoreClass>) {
        this.items = items
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}