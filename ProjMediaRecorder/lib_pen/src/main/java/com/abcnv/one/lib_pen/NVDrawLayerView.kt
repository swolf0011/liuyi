package com.abcnv.one.lib_pen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceView
import com.abcnv.one.lib_pen.data.NvPenType
import com.abcnv.one.lib_pen.data.NvStroke
import com.abcnv.one.lib_pen.pen.*

class NVDrawLayerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SurfaceView(context, attrs, defStyleAttr, defStyleRes) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawHandlers(it)
        }
    }

    var strokeList = mutableListOf<NvStroke>()
    val mainPaint = Paint()
    val highlighterPaint = Paint()
    val path = Path()

    fun drawHandlers(canvas: Canvas) {
        strokeList.forEach {
            drawStroke(canvas, it)
        }
    }

    fun drawStroke(canvas: Canvas, stroke: NvStroke) {
        when (stroke.penType) {
            NvPenType.BallpointPen -> {
                NvBallpointPen.draw(canvas, mainPaint, path, stroke)
            }
            NvPenType.GraphPen -> {
                NvGraphPen.draw(canvas, mainPaint, path, stroke)
            }
            NvPenType.HighlighterPen -> {
                NvHighlighterPen.draw(canvas, mainPaint, highlighterPaint, path, stroke)
            }
            NvPenType.Pen -> {
                NvPen.draw(canvas, mainPaint, path, stroke)
            }
            NvPenType.Pencil -> {
                NvPencil.draw(canvas, mainPaint, path, stroke)
            }
            NvPenType.PicturePen -> {
                NvPicturePen.draw(canvas, mainPaint, stroke)
            }
            NvPenType.TxtPen -> {
                NvTxtPen.draw(canvas, mainPaint, stroke)
            }
        }
    }
}