package com.abcnv.nvone.biz_db.repository

import android.content.Context
import com.abcnv.nvone.biz_db.NvAppDB
import com.abcnv.nvone.biz_db.dao.UserDao

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class UserRepository private constructor(private val userDao: UserDao) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(appContext: Context) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(NvAppDB.getInstance(appContext).getUserDao()).also { instance = it }
            }
    }
}