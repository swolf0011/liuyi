package com.abcnv.nvone.lib_base.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.abcnv.nvone.lib_base.util.NvMeasureSpecUtil

object NvPopWinUtil {
    fun showAsDropDown(
        parentView: View,
        popupView: View,
        offX: Int = 0,
        offY: Int = 0
    ): PopupWindow {
        val wrap_content = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, wrap_content, wrap_content, true)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.contentView.measure(
            NvMeasureSpecUtil.makeDropDownMeasureSpec(popupWindow.width),
            NvMeasureSpecUtil.makeDropDownMeasureSpec(popupWindow.height)
        )
        popupWindow.showAsDropDown(parentView, offX, offY)
        return popupWindow
    }

    fun showAtLocation(
        parentView: View,
        popupView: View,
        gravity: Int = Gravity.BOTTOM,
        offX: Int = 0,
        offY: Int = 0
    ): PopupWindow {
        val wrap_content = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, wrap_content, wrap_content, true)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.contentView.measure(
            NvMeasureSpecUtil.makeDropDownMeasureSpec(popupWindow.width),
            NvMeasureSpecUtil.makeDropDownMeasureSpec(popupWindow.height)
        )
        popupWindow.showAtLocation(parentView, gravity, offX, offY)
        return popupWindow
    }
}