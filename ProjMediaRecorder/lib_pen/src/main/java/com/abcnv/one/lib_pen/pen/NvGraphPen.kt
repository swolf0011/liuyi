package com.abcnv.one.lib_pen.pen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.abcnv.one.lib_pen.data.NvStroke

object NvGraphPen {
    fun draw(canvas: Canvas, paint: Paint, path: Path, stroke: NvStroke) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = stroke.strokeWidth
        paint.color = Color.parseColor(stroke.c0)

        val size = stroke.strokePointList.size

        path.reset()


    }
}