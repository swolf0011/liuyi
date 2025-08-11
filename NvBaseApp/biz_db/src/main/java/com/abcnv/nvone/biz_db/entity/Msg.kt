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
@Entity(tableName = "Msg")
data class Msg(
    @PrimaryKey @ColumnInfo(name = "id", typeAffinity = ColumnInfo.TEXT)
    var id: String = "",
    @ColumnInfo(name = "msg", typeAffinity = ColumnInfo.TEXT)
    var msg: String = "",
    @ColumnInfo(name = "linkUrl", typeAffinity = ColumnInfo.TEXT)
    var linkUrl: String = "",
    @ColumnInfo(name = "time", typeAffinity = ColumnInfo.TEXT)
    var time: String = "",//YYYY-MM-DD HH:mm:ss
    @ColumnInfo(name = "fromUid", typeAffinity = ColumnInfo.TEXT)
    var fromUid: String = "",
    @ColumnInfo(name = "toUids", typeAffinity = ColumnInfo.TEXT)
    var toUids: String = "[]"//[uid1,uid2,uid3]
) : Serializable
