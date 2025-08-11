package com.abcly.swolf.lib_util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

object NYKeyboardUtil {
//    fun getKeyboardHeight(activity: Activity): Int {
//        val rootView = activity.window.decorView
//        val rect = Rect()
//        rootView.getWindowVisibleDisplayFrame(rect)
//        val visibleHeight = (rect.bottom - rect.top)
//        //粗略计算高度的变化值，后面会根据状态栏和导航栏修正
//        val heightDiff = rootView.height - visibleHeight
//        //这里取了一个大概值，当窗口高度变化值超过屏幕的 1/3 时，视为软键盘弹出
//        var keyboardHeight: Int
//        if (heightDiff > activity.screenHeight / 4) {
//            val isPhone = NYDeviceUtil.isPad(activity)
//            //非全屏时减去状态栏高度
//            keyboardHeight = if (activity.isFullScreen || (activity.isLandscape && isPhone)) {
//                heightDiff
//            } else {
//                heightDiff - activity.statusBarHeight
//            }
//            //导航栏显示时减去其高度，但横屏时导航栏在侧边，故不必扣除高度
//        } else {
//            //软键盘隐藏时键盘高度为0
//            keyboardHeight = 0
//        }
//        return keyboardHeight
//    }



//    /**
//     * 当前虚拟导航栏是否显示
//     */
//    fun isNavBarShowed(activity: Activity): Boolean {
//        val display = activity.windowManager.defaultDisplay
//        val size = Point()
//        val realSize = Point()
//        display.getSize(size)
//        display.getRealSize(realSize)
//        return realSize.y != size.y && (realSize.y - size.y != activity.statusBarHeight)
//    }

    /**
     * 获取屏幕高度
     */
    private val Context.screenHeight: Int
        get() {
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            wm.defaultDisplay.getRealSize(point)
            return point.y
        }

    /**
     * 判断和设置是否全屏，赋值为true设置成全屏
     */
    private val Activity.isFullScreen: Boolean
        get() {
            val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
            return (window.attributes.flags and flag) == flag
        }
    /**
     * 是否是横屏
     */
    private val Activity.isLandscape: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

//    private val Context.statusBarHeight: Int get() = BarUtils.getStatusBarHeight()


    /**
     * 判断键盘是否显示（当前view是否在输入法活动状态，即是否正在接收软键盘输入）。
     *
     * @param context 上下文
     * @param view    当前聚焦的正在接收软件盘输入的View。
     */
    fun isSoftInputShowing(context: Context,view: View?): Boolean {
        var bool = false
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive(view)) {
            bool = true
        }
        return bool
    }
    /**
     * 显示键盘
     *
     * @param context 上下文
     * @param view    当前聚焦的正在接收软件盘输入的View，通常为EditText。
     * 注:需要view绘制完成后调用才生效,建议使用view.post{}调用
     */
    fun showSoftInput(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }


    /**
     * 隐藏输入法
     *
     * @param context 上下文
     * @param view    可以是任意已添加到window中的View（已添加到布局中的View）。
     */
    fun hideSoftInput(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            //第二个参数为flags, 0 | InputMethodManager.HIDE_IMPLICIT_ONLY | InputMethodManager.HIDE_NOT_ALWAYS
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}