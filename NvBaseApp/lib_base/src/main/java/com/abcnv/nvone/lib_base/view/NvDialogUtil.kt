package com.abcnv.nvone.lib_base.view


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.abcnv.nvone.lib_base.R

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
object NvDialogUtil {

    /**
     */
    fun show(context: Context, view: View, cancelable: Boolean, theme: Int = 0): Dialog {
        val dialog = if (theme > 0) {
            Dialog(context, theme)
        } else {
            Dialog(context, R.style.Theme_NvBaseApp)
        }
        val drawable = ColorDrawable()
        drawable.alpha = 0
        dialog.window?.setBackgroundDrawable(drawable)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.setCancelable(cancelable)
        dialog.show()
        return dialog
    }

}