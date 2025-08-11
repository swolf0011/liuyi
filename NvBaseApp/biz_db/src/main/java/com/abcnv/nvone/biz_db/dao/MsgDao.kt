package com.abcnv.nvone.biz_db.dao

import androidx.room.*
import com.abcnv.nvone.biz_db.entity.Msg

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
@Dao
interface MsgDao {
    @Insert
    fun insert(msg: Msg)

    @Insert
    fun inserts(list: List<Msg>)

    @Update
    fun update(msg: Msg)

    @Update
    fun updates(list: List<Msg>)

    @Delete
    fun delete(msg: Msg)

    @Delete
    fun deletes(list: List<Msg>)

    @Query("SELECT * FROM Msg")
    fun getAll(): List<Msg>

    @Query("SELECT * FROM Msg WHERE :where")
    fun getsOfWhere(where: String): List<Msg>

    @Query("SELECT * FROM Msg ORDER BY :order")
    fun getsOfOrder(order: String): List<Msg>

    @Query("SELECT * FROM Msg WHERE :where ORDER BY :order")
    fun getsOfWhereOrder(where: String, order: String): List<Msg>

    @Query("SELECT * FROM Msg GROUP BY :group ORDER BY :order")
    fun getsOfGroupOrder(group: String, order: String): List<Msg>

    @Query("SELECT * FROM Msg WHERE :where GROUP BY :group ORDER BY :order")
    fun getsOfWhereGroupOrder(where: String, group: String, order: String): List<Msg>

    @Query("SELECT * FROM Msg WHERE :where GROUP BY :group HAVING :having ORDER BY :order")
    fun getsOfWhereGroupHavingOrder(
        where: String,
        group: String,
        having: String,
        order: String
    ): List<Msg>
}