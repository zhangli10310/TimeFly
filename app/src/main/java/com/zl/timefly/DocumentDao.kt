package com.zl.timefly

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 *
 *<p></p>
 *
 * Created by zhangli on 2018/3/5 10:19.<br/>
 */
@Dao
interface DocumentDao {

    @Query("SELECT * FROM Documents")
    fun getAll(): List<DocumentItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: DocumentItem)

    @Query("DELETE FROM Documents")
    fun deleteAll()
}