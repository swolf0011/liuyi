package com.abcnv.nvone.lib_base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.abcnv.nvone.lib_base.vm.NvBaseViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
@AndroidEntryPoint
abstract class NvBaseActivity<V : ViewDataBinding, VM : NvBaseViewModel> : AppCompatActivity() {
    private val TAG = "0011==${javaClass.simpleName}"

    //本Activity
    lateinit var mActivity: NvBaseActivity<V, VM>

    //DataBinding
    protected lateinit var mBinding: V

    //ViewModel
    protected lateinit var mVM: VM

    //设备布局
    abstract val mLayoutID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "=>onCreate")
//        super.requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
//        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏显示
//        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 高亮显示

//        setContentView(setLayout())
        mActivity = this
        initViewModel()
        initDataBinding()
        getIntentInData()
        initData()
        initView()
        initListener()

        pri
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "=>onRestart")
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
    private fun initDataBinding() {
        mBinding = DataBindingUtil.setContentView<V>(this, mLayoutID)
        mBinding.lifecycleOwner = this
    }

    /**
     * 初始化viewModel :
     * mVM = NvVMFactory.create(this, MainVM::class.java)
     */
    abstract fun initViewModel()

    /**
     * 初始化Intent数据
     */
    abstract fun getIntentInData()

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