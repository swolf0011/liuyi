package com.abcnv.nvone.lib_util

import android.graphics.*
import android.graphics.drawable.Drawable

object NvPhotoEditUtil {


    /**
     * 图片透明度处理
     *
     * @param sourceImg 原始图片
     * @param number    透明度
     * @return
     */
    fun setAlpha(sourceImg: Bitmap, number: Int): Bitmap {
        var sourceImg = sourceImg
        var number = number
        val argb = IntArray(sourceImg.width * sourceImg.height)
        sourceImg.getPixels(
            argb,
            0,
            sourceImg.width,
            0,
            0,
            sourceImg.width,
            sourceImg.height
        )// 获得图片的ARGB值
        number = number * 255 / 100
        for (i in argb.indices) {
            argb[i] = number shl 24 or (argb[i] and 0x00FFFFFF)// 修改最高2位的值
        }
        sourceImg =
            Bitmap.createBitmap(argb, sourceImg.width, sourceImg.height, Bitmap.Config.ARGB_8888)
        return sourceImg
    }


    /***
     * 图片平均分割方法，将大图平均分割为N行N列，方便用户使用
     *
     * @param g 画布
     * @param paint 画笔
     * @param imgBit 图片
     * @param x X轴起点坐标
     * @param y Y轴起点坐标
     * @param w 单一图片的宽度
     * @param h 单一图片的高度
     * @param line 第几列
     * @param row 第几行
     */
    fun cuteImage(
        g: Canvas, paint: Paint, imgBit: Bitmap, x: Int,
        y: Int, w: Int, h: Int, line: Int, row: Int
    ) {
        g.clipRect(x, y, x + w, h + y)
        g.drawBitmap(imgBit, (x - line * w).toFloat(), (y - row * h).toFloat(), paint)
        g.restore()
    }

    /**
     * 将一个图片切割成多个图片
     *
     * @param bitmap
     * @param xPiece
     * @param yPiece
     * @return
     */
    fun split(bitmap: Bitmap, xPiece: Int, yPiece: Int): List<Bitmap> {
        val pieces = mutableListOf<Bitmap>()
        val width = bitmap.width
        val height = bitmap.height
        val pieceWidth = width / 3
        val pieceHeight = height / 3
        for (i in 0 until yPiece) {
            for (j in 0 until xPiece) {
                val xValue = j * pieceWidth
                val yValue = i * pieceHeight
                val bitmap = Bitmap.createBitmap(
                    bitmap, xValue, yValue,
                    pieceWidth, pieceHeight
                )
                pieces.add(bitmap)
            }
        }
        return pieces
    }

    /***
     * 绘制带有边框的文字
     *
     * @param strMsg 绘制内容
     * @param g 画布
     * @param paint 画笔
     * @param setx X轴起始坐标
     * @param sety Y轴的起始坐标
     * @param fg 前景色
     * @param bg 背景色
     */
    fun drawText(
        strMsg: String, g: Canvas, paint: Paint, setx: Int,
        sety: Int, fg: Int, bg: Int
    ) {
        paint.color = bg
        g.drawText(strMsg, (setx + 1).toFloat(), sety.toFloat(), paint)
        g.drawText(strMsg, setx.toFloat(), (sety - 1).toFloat(), paint)
        g.drawText(strMsg, setx.toFloat(), (sety + 1).toFloat(), paint)
        g.drawText(strMsg, (setx - 1).toFloat(), sety.toFloat(), paint)
        paint.color = fg
        g.drawText(strMsg, setx.toFloat(), sety.toFloat(), paint)
        g.restore()
    }

    /**
     * 图标加灰色过滤；
     *
     * @param mDrawable
     * @return
     */
    fun convertGrey(drawable: Drawable): Drawable {
        drawable.mutate()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val cf = ColorMatrixColorFilter(cm)
        drawable.colorFilter = cf
        return drawable
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    fun convertGrey(img: Bitmap): Bitmap {
        val width = img.width         //获取位图的宽
        val height = img.height       //获取位图的高
        val pixels = IntArray(width * height) //通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height)
        val alpha = 0xFF shl 24
        for (i in 0 until height) {
            for (j in 0 until width) {
                var grey = pixels[width * i + j]
                val red = grey and 0x00FF0000 shr 16
                val green = grey and 0x0000FF00 shr 8
                val blue = grey and 0x000000FF
                grey = (red.toFloat() * 0.3 + green.toFloat() * 0.59 + blue.toFloat() * 0.11).toInt()
                grey = alpha or (grey shl 16) or (grey shl 8) or grey
                pixels[width * i + j] = grey
            }
        }
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }



}
