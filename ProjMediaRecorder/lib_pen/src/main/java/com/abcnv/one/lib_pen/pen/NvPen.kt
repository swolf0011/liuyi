package com.abcnv.one.lib_pen.pen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.abcnv.one.lib_pen.data.NvStroke
import com.abcnv.one.lib_pen.data.NvStrokePoint
import kotlin.math.abs
import kotlin.math.sqrt

object NvPen {
    fun draw(canvas: Canvas, paint: Paint,path: Path,  stroke: NvStroke) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 1f
        paint.color = Color.parseColor(stroke.c0)

        val size = stroke.strokePointList.size

        var previousStrokePoint = NvStrokePoint(0f, 0f, 1f)

        stroke.strokePointList.forEachIndexed { index, strokePoint ->
            when (index) {
                0 -> {
                    previousStrokePoint = strokePoint
                    path.moveTo(strokePoint.x, strokePoint.y)
                    path.addCircle(strokePoint.x, strokePoint.y, strokePoint.r, Path.Direction.CW)
                }
                size - 1 -> {
                    path.lineTo(strokePoint.x, strokePoint.y)
                    path.addCircle(strokePoint.x, strokePoint.y, strokePoint.r, Path.Direction.CW)
                    drawTrapezoid(path, previousStrokePoint, strokePoint)
                }
                else -> {
                    path.lineTo(strokePoint.x, strokePoint.y)
                    path.addCircle(strokePoint.x, strokePoint.y, strokePoint.r, Path.Direction.CW)
                    drawTrapezoid(path, previousStrokePoint, strokePoint)
                    previousStrokePoint = strokePoint
                }
            }
        }
        canvas.drawPath(path, paint)
    }

    private fun drawTrapezoid(
        path: Path,
        previous: NvStrokePoint,
        current: NvStrokePoint
    ) {
        val previous_x = previous.x
        val previous_y = previous.y
        val current_x = current.x
        val current_y = current.y

        val a = abs(previous_x - current_x)
        val b = abs(previous_y - current_y)
        val c = sqrt(a * a + b * b)

        var previous_0_x = 0f
        var previous_0_y = 0f
        var previous_1_x = 0f
        var previous_1_y = 0f

        var current_2_x = 0f
        var current_2_y = 0f
        var current_3_x = 0f
        var current_3_y = 0f

        val previous_len_x = previous.r * b / c
        val previous_len_y = previous.r * b / c

        val current_len_x = current.r * b / c
        val current_len_y = current.r * b / c

        if (previous_x < current_x) {
            //向右
            if (previous_y < current_y) {
                //向右下
                previous_0_x = previous_x - previous_len_x
                previous_0_y = previous_y + previous_len_y
                previous_1_x = previous_x + previous_len_x
                previous_1_y = previous_y - previous_len_y

                current_2_x = current_x + current_len_x
                current_2_y = current_y - current_len_y
                current_3_x = current_x - current_len_x
                current_3_y = current_y + current_len_y
            } else if (previous_y > current_y) {
                //向右上
                previous_0_x = previous_x - previous_len_x
                previous_0_y = previous_y - previous_len_y
                previous_1_x = previous_x + previous_len_x
                previous_1_y = previous_y + previous_len_y

                current_2_x = current_x + current_len_x
                current_2_y = current_y + current_len_y
                current_3_x = current_x - current_len_x
                current_3_y = current_y - current_len_y
            } else {
                //向右
                previous_0_x = previous_x
                previous_0_y = previous_y - previous.r
                previous_1_x = previous_x
                previous_1_y = previous_y + previous.r

                current_2_x = current_x
                current_2_y = current_y + current.r
                current_3_x = current_x
                current_3_y = current_y - current.r
            }
        } else if (previous_x > current_x) {
            //向左
            if (previous_y < current_y) {
                //向左下
                previous_0_x = previous_x - previous_len_x
                previous_0_y = previous_y - previous_len_y
                previous_1_x = previous_x + previous_len_x
                previous_1_y = previous_y + previous_len_y

                current_2_x = current_x + current_len_x
                current_2_y = current_y + current_len_y
                current_3_x = current_x - current_len_x
                current_3_y = current_y - current_len_y
            } else if (previous_y > current_y) {
                //向左上
                previous_0_x = previous_x - previous_len_x
                previous_0_y = previous_y + previous_len_y
                previous_1_x = previous_x + previous_len_x
                previous_1_y = previous_y - previous_len_y

                current_2_x = current_x + current_len_x
                current_2_y = current_y - current_len_y
                current_3_x = current_x - current_len_x
                current_3_y = current_y + current_len_y
            } else {
                //向左
                previous_0_x = previous_x
                previous_0_y = previous_y - previous.r
                previous_1_x = previous_x
                previous_1_y = previous_y + previous.r

                current_2_x = current_x
                current_2_y = current_y + current.r
                current_3_x = current_x
                current_3_y = current_y - current.r
            }
        } else {
            if (previous_y != current_y) {
                //向下 / 向上
                previous_0_x = previous_x - previous.r
                previous_0_y = previous_y
                previous_1_x = previous_x + previous.r
                previous_1_y = previous_y

                current_2_x = current_x + current.r
                current_2_y = current_y
                current_3_x = current_x - current.r
                current_3_y = current_y
            } else {
                //y不变,,这个点没有移动，不操作
            }
        }
        if(previous_0_x != 0f && previous_0_y != 0f &&
            previous_1_x != 0f && previous_1_y != 0f &&
            current_2_x != 0f && current_2_y != 0f &&
            current_3_x != 0f && current_3_y != 0f){
            createTrapezoidPath(
                path,
                previous_0_x,
                previous_0_y,
                previous_1_x,
                previous_1_y,
                current_2_x,
                current_2_y,
                current_3_x,
                current_3_y
            )
        }
    }

    private fun createTrapezoidPath(
        path: Path,
        previous_0_x: Float, previous_0_y: Float,
        previous_1_x: Float, previous_1_y: Float,
        current_2_x: Float, current_2_y: Float,
        current_3_x: Float, current_3_y: Float
    ) {
        path.moveTo(previous_0_x, previous_0_y)
        path.lineTo(previous_1_x, previous_1_y)
        path.lineTo(current_2_x, current_2_y)
        path.lineTo(current_3_x, current_3_y)
        path.lineTo(previous_0_x, previous_0_y)
    }
}