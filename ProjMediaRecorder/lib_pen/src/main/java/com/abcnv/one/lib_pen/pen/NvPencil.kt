package com.abcnv.one.lib_pen.pen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.abcnv.one.lib_pen.data.NvStroke

object NvPencil {
    fun draw(canvas: Canvas, paint: Paint, path: Path, stroke: NvStroke) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = stroke.strokeWidth
        paint.color = Color.parseColor(stroke.c0)

        val size = stroke.strokePointList.size

        path.reset()

        stroke.strokePointList.forEachIndexed { index, strokePoint ->
            when (index) {
                0 -> {
                    path.moveTo(strokePoint.x, strokePoint.y)
                    path.addCircle(strokePoint.x, strokePoint.y, strokePoint.r, Path.Direction.CW)
                }
                size - 1 -> {
                    path.lineTo(strokePoint.x, strokePoint.y)
                }
                else -> {
                    path.lineTo(strokePoint.x, strokePoint.y)
                }
            }
        }

        canvas.drawPath(path, paint)
    }
}