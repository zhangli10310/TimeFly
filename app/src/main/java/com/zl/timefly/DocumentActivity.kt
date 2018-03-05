package com.zl.timefly

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_document.*
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
        mAdapter = DocumentAdapter(mList)
        recyclerView.adapter = mAdapter


        loadData()
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
        thread {
            AppDatabase.getInstance(this)
                    .documentDao()
                    .insertOne(DocumentItem(0, "3.04",
                            "添加于" + SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())))
            loadData()
        }
    }

}