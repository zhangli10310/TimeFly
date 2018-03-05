package com.zl.timefly

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 *
 *<p></p>
 *
 * Created by zhangli on 2018/3/5 10:14.<br/>
 */
@Database(entities = arrayOf(DocumentItem::class), version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun documentDao(): DocumentDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "time.db")
                        .build()
    }

}