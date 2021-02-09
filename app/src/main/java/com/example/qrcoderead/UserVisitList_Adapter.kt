package com.example.qrcoderead

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.uservisitelist_item.*

class VisitAdapter: RecyclerView.Adapter<VisitAdapter.ViewHolder>() {
    private var items = mutableListOf<UserDataClass>()
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

        holder.apply {
            bindView(item.getData(InfoItem.가게명))
            bindView(item.getData(InfoItem.방명록))
        }
    }


    fun replaceItems(items: MutableList<UserDataClass>) {
        this.items = items
        notifyDataSetChanged()
    }

    //아이템 갯수 리턴처리
    override fun getItemCount(): Int {
        return items.size
    }

    //개별 아이템의 뷰를 제공
    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView),LayoutContainer {

        override val containerView: View?
            get() = containerView

        fun bindView(name : String) {
            tv_test.text = name
            tv_test2.text = name
        }
    }
}