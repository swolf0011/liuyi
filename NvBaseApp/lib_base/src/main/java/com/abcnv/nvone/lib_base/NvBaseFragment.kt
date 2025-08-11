package com.abcnv.nvone.lib_base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.abcnv.nvone.lib_base.vm.NvBaseViewModel
/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */

abstract class NvBaseFragment<V : ViewDataBinding, VM : NvBaseViewModel> : Fragment() {
    private var TAG = "0011==${javaClass.simpleName}"

    //本Fragment
    lateinit var mFragment: NvBaseFragment<V, VM>

    //DataBinding
    protected lateinit var mBinding: V

    //ViewModel
    protected lateinit var mVM: VM

    //设备布局
    abstract val layoutID: Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "=>onCreateView")
        mFragment = this
        initViewModel()
        initDataBinding(inflater, container)
        initData()
        initView()
        initListener()
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "=>onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "=>onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "=>onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "=>onDestroy")
    }

    /**
     * 初始化DataBinding
     */
    private fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = DataBindingUtil.inflate(inflater, layoutID, container, false)
        mBinding.lifecycleOwner = this
    }

    /**
     * 初始化viewModel :
     * mVM = NvVMFactory.create(this, MainVM::class.java)
     */
    abstract fun initViewModel()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 初始化Value
     */
    abstract fun initListener()

}