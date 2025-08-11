package com.abcnv.nvone.biz_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
@Entity(tableName = "User")
data class User(
    @PrimaryKey @ColumnInfo(name = "id", typeAffinity = ColumnInfo.TEXT)
    var id: String = "",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String = "",
    @ColumnInfo(name = "pwd", typeAffinity = ColumnInfo.TEXT)
    var pwd: String = "",
    @ColumnInfo(name = "phone", typeAffinity = ColumnInfo.TEXT)
    var phone: String = "",
    @ColumnInfo(name = "img", typeAffinity = ColumnInfo.TEXT)
    var img: String = ""
) : Serializable
