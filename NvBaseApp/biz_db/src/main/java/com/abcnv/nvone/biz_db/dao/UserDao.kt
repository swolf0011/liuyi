package com.abcnv.nvone.biz_db.dao

import androidx.room.*
import com.abcnv.nvone.biz_db.entity.User

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
interface UserDao {
    @Insert
    fun insert(user: User)

    @Insert
    fun inserts(list: List<User>)

    @Update
    fun update(user: User)

    @Update
    fun updates(list: List<User>)

    @Delete
    fun delete(user: User)

    @Delete
    fun deletes(list: List<User>)

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE :where")
    fun getsOfWhere(where: String): List<User>

    @Query("SELECT * FROM User ORDER BY :order")
    fun getsOfOrder(order: String): List<User>

    @Query("SELECT * FROM User WHERE :where ORDER BY :order")
    fun getsOfWhereOrder(where: String, order: String): List<User>

    @Query("SELECT * FROM User GROUP BY :group ORDER BY :order")
    fun getsOfGroupOrder(group: String, order: String): List<User>

    @Query("SELECT * FROM User WHERE :where GROUP BY :group ORDER BY :order")
    fun getsOfWhereGroupOrder(where: String, group: String, order: String): List<User>

    @Query("SELECT * FROM User WHERE :where GROUP BY :group HAVING :having ORDER BY :order")
    fun getsOfWhereGroupHavingOrder(
        where: String,
        group: String,
        having: String,
        order: String
    ): List<User>
}