package com.abcnv.nvone.lib_base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
@HiltViewModel
open class NvBaseViewModel : ViewModel() {

    val id = MutableLiveData<Int>()

    fun vmScopeIO2Main(callbalckIo: () -> Unit, callbalckMain: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            callbalckIo.invoke()
            withContext(Dispatchers.Main) {
                callbalckMain.invoke()
            }
        }
    }

    fun vmScopeIO(ioRunFun: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            ioRunFun.invoke()
        }
    }

    fun vmScopeMain(ioRunFun: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            ioRunFun.invoke()
        }
    }
}