package com.zl.timefly

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_document.*
import kotlinx.android.synthetic.main.layout_add_document.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by zhangli on 2018/3/4,23:13<br/>
 */
class DocumentActivity : AppCompatActivity() {

    val mList: MutableList<DocumentItem> = mutableListOf()
    lateinit var mAdapter: DocumentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        floatingActionBar.setOnClickListener {
            add()
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = DocumentAdapter(mList, {
            val item = mList[it.adapterPosition]
            AlertDialog.Builder(this)
                    .setTitle(item.date)
                    .setMessage(item.content)
                    .setPositiveButton("edit", { _, _ ->
                        edit(item)
                    })
                    .setNegativeButton("cancel", null)
                    .show()
        },{
            AlertDialog.Builder(this)
                    .setMessage("删除?")
                    .setPositiveButton("ok", { _, _ ->
                        deleteItem(mList[it.adapterPosition])
                    })
                    .setNegativeButton("cancel", null)
                    .show()
            return@DocumentAdapter true
        })
        recyclerView.adapter = mAdapter

        loadData()
    }

    private fun edit(item: DocumentItem) {

        val view = LayoutInflater.from(this).inflate(R.layout.layout_add_document, null)
        view.dateEdit.setText(item.date)
        view.contentEdit.setText(item.content)
        AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("ok", { _, _ ->
                    item.date = view.dateEdit.text.toString().trim()
                    item.content = view.contentEdit.text.toString().trim()
                    insert(item)
                })
                .setNegativeButton("cancel", null)
                .show()
    }

    private fun insert(item: DocumentItem) {

        thread {
            AppDatabase.getInstance(this)
                    .documentDao()
                    .insertOne(item)
            loadData()
        }
    }

    private fun deleteItem(item: DocumentItem) {
        thread {
            AppDatabase.getInstance(this).documentDao().deleteOne(item.id)
            loadData()
        }
    }

    private fun loadData() {

        thread {
            val all = AppDatabase.getInstance(this).documentDao().getAll()
            mList.clear()
            mList.addAll(all)
            runOnUiThread {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun add() {

        val view = LayoutInflater.from(this).inflate(R.layout.layout_add_document, null)
        view.dateEdit.setText(SimpleDateFormat("MM-dd HH:m", Locale.getDefault()).format(System.currentTimeMillis()))
        AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("ok", { _, _ ->
                    insert(DocumentItem(0, view.dateEdit.text.toString().trim(), view.contentEdit.text.toString().trim()))
                })
                .setNegativeButton("cancel", null)
                .show()
    }

}