package com.abcnv.nvone.lib_base.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog

class NvBaseBottomSheetDialog(activity: Activity, val mContentView: View) :
    BottomSheetDialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mContentView)
        setCanceledOnTouchOutside(false)
    }

}