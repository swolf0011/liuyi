package com.abcnv.nvone.biz_db.repository

import android.content.Context
import com.abcnv.nvone.biz_db.NvAppDB
import com.abcnv.nvone.biz_db.dao.MsgDao

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class MsgRepository private constructor(private val msgDao: MsgDao) {

    companion object {
        private var instance: MsgRepository? = null
        fun getInstance(appContext: Context) =
            instance ?: synchronized(this) {
                instance ?: MsgRepository(NvAppDB.getInstance(appContext).getMsgDao()).also { instance = it }
            }
    }
}