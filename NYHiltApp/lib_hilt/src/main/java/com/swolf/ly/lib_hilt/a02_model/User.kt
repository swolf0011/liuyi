package com.swolf.ly.lib_hilt.a02_model

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @Inject 告诉Hilt 生成实体，可以供注入。
 * @ActivityContext 获取Activity上下文件。
 *
 * @property context Context
 * @constructor
 */
class User @Inject constructor(@ActivityContext val context: Context){
    var name = ""
    var age = 0

    override fun toString(): String {
        return "User:${name},${age}"
    }

    fun show() = Toast.makeText(context, toString(), Toast.LENGTH_LONG).show()
}