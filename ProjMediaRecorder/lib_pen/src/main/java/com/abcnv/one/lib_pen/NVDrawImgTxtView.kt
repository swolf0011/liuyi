package com.abcnv.one.lib_pen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceView
import android.widget.FrameLayout
import com.abcnv.one.lib_pen.data.NvPenType
import com.abcnv.one.lib_pen.data.NvStroke
import com.abcnv.one.lib_pen.data.NvStrokeImgTxt
import com.abcnv.one.lib_pen.pen.*

class NVDrawImgTxtView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    var strokeImgTxtList = mutableListOf<NvStrokeImgTxt>()

    fun showImgTxt(){
        strokeImgTxtList.forEach {

        }
    }



}