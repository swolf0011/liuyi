package com.abcnv.one.lib_pen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.abcnv.one.lib_pen.data.NvPenType
import com.abcnv.one.lib_pen.data.NvStroke
import com.abcnv.one.lib_pen.data.NvStrokeImgTxt
import com.abcnv.one.lib_pen.pen.*

class NVImageView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var strokeImg: NvStrokeImgTxt? = null
    var isEdit = false

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (isEdit && strokeImg != null && canvas != null) {
            drawPathEffect(canvas!!, strokeImg!!)
        }
    }



    val paint = Paint()

    init {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = Color.parseColor("#ff00ff")

        val floatArray = FloatArray(2)
        floatArray[0] = 5f
        floatArray[1] = 5f
        val effects = DashPathEffect(floatArray, 4f)
        paint.pathEffect = effects
    }


    fun drawPathEffect(canvas: Canvas, stroke: NvStrokeImgTxt) {
        val path = Path()
        path.moveTo(0f, 0f)
        path.moveTo(strokeImg!!.w + 0f, 0f)
        path.moveTo(strokeImg!!.w + 0f, stroke!!.h + 0f)
        path.moveTo(0f, stroke!!.h + 0f)
        path.close()
        canvas.drawPath(path, paint)
    }
}