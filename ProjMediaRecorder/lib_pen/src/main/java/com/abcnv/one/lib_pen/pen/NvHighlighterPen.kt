package com.abcnv.one.lib_pen.pen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.abcnv.one.lib_pen.data.NvStroke

object NvHighlighterPen {
    fun draw(canvas: Canvas, paint: Paint, highlighterPaint: Paint, path: Path, stroke: NvStroke) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = stroke.strokeWidth
        paint.color = Color.parseColor(stroke.c0)

        highlighterPaint.reset()
        highlighterPaint.isAntiAlias = true
        highlighterPaint.style = Paint.Style.STROKE
        highlighterPaint.strokeWidth = stroke.strokeWidth + 5
        highlighterPaint.color = Color.parseColor(stroke.c1)

        val size = stroke.strokePointList.size

        path.reset()

        stroke.strokePointList.forEachIndexed { index, strokePoint ->
            when (index) {
                0 -> {
                    path.moveTo(strokePoint.x, strokePoint.y)
                }
                size - 1 -> {
                    path.lineTo(strokePoint.x, strokePoint.y)
                }
                else -> {
                    path.lineTo(strokePoint.x, strokePoint.y)
                }
            }
        }

        canvas.drawPath(path, highlighterPaint)
        canvas.drawPath(path, paint)
    }
}