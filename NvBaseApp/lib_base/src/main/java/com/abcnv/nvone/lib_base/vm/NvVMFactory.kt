package com.abcnv.nvone.lib_base.vm

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
object NvVMFactory {
    fun <E : ViewModel> create(store: ViewModelStoreOwner, clazz: Class<E>): E {
        return ViewModelProvider(store).get(clazz)
    }

    private fun test(activity: AppCompatActivity) {
        create(activity, ViewModel::class.java)
    }
}