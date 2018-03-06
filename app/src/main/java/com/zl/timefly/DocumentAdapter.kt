package com.zl.timefly

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_document.view.*

/**
 * Created by zhangli on 2018/3/4,23:31<br/>
 */
class DocumentAdapter(var list: List<DocumentItem>, private var onItemClick: ((holder: ViewHolder) -> Unit)? = null, private var onItemLongClick: ((holder: ViewHolder) -> Boolean)? = null) : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.date.text = list[position].date
        holder.itemView.content.text = list[position].content

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(holder)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener onItemLongClick?.invoke(holder) ?: false
        }
    }

    override fun getItemCount() = list.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}