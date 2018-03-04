package com.zl.timefly

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_document.view.*

/**
 * Created by zhangli on 2018/3/4,23:31<br/>
 */
class DocumentAdapter(var list: List<DocumentItem>) : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_document, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.date.text = list[position].date
        holder.itemView.content.text = list[position].content
    }

    override fun getItemCount() = list.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}