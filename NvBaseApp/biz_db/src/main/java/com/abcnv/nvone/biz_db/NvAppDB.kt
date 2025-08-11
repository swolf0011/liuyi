package com.abcnv.nvone.biz_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abcnv.nvone.biz_db.dao.MsgDao
import com.abcnv.nvone.biz_db.dao.UserDao
import com.abcnv.nvone.biz_db.entity.Msg
import com.abcnv.nvone.biz_db.entity.User

@Database( entities = [User::class, Msg::class],
    version = NvAppDB.DB_VERSION,
    exportSchema = false)
abstract class NvAppDB: RoomDatabase()  {
    abstract fun getUserDao(): UserDao
    abstract fun getMsgDao(): MsgDao
    companion object {
        @Volatile
        private var instance: NvAppDB? = null
        const val DB_NAME = "AppDB_db"
        const val DB_VERSION = 2
        fun getInstance(appContext: Context): NvAppDB {
            instance?:synchronized(this) {
                instance?:databaseBuilder(appContext).also { instance = it }
            }
            return instance!!
        }
        private fun databaseBuilder(appContext: Context): NvAppDB {
            return Room.databaseBuilder(appContext.applicationContext, NvAppDB::class.java, DB_NAME)
			    .fallbackToDestructiveMigration()
                .addCallback(CALLBACK)
				//是否允许在主线程进行查询
                .allowMainThreadQueries()
				// 设置升级策略，失败会回滚到上一个版本
                .fallbackToDestructiveMigration()
                .addMigrations(
                    MIGRATION_1_2,
                ).build()
        }
        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //事务处理
//                db.execSQL(triggerSQL)
            }
        }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE User ADD COLUMN type INTEGER NOT NULL DEFAULT(1)"
                )
            }
        }

    }
}