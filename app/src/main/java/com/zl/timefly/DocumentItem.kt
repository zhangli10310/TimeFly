package com.zl.timefly

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by zhangli on 2018/3/4,23:33<br/>
 */
@Entity(tableName = "Documents")
data class DocumentItem(@PrimaryKey(autoGenerate = true)
                        var id: Int,
                        var date: String,
                        var content: String?)