package com.abcnv.nvone.mediaprojectionapp.util

import android.graphics.*
import android.media.ExifInterface
import java.io.*


object NvBitmapUtil {
    /**
     * 圆形图片
     *
     * @param source      位图
     * @param strokeWidth 裁剪范围 0表示最大
     * @param bl          是否需要描边
     * @param edge          画笔粗细
     * @param color          颜色代码
     * @return bitmap
     */
    fun circleBitmap(
        bitmap: Bitmap,
        strokeWidth: Float,
        bl: Boolean,
        edge: Float = 0f,
        color: Int
    ): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        val color0 = if (color == 0) {
            -0x15db8 //默认橘黄色
        } else {
            color
        }
        val diameter = Math.min(bitmap.width, bitmap.height)
        val target = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target) //创建画布
        val paint = Paint()
        paint.isAntiAlias = true
        if (bl) {
            paint.color = color0
            paint.style = Paint.Style.STROKE //描边
            paint.strokeWidth = edge
        }
        val cx = bitmap.width / 2f
        val cy = bitmap.height / 2f
        val r = diameter / 2f
        canvas.drawCircle(cx, cy, r, paint) //绘制圆形
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) //取相交裁剪
        canvas.drawBitmap(bitmap, strokeWidth, strokeWidth, paint)
        return target
    }

    /**
     * 圆角图片
     *
     * @param bitmap 位图
     * @param rx     x方向上的圆角半径
     * @param ry     y方向上的圆角半径
     * @param bl     是否需要描边
     * @param edge     画笔粗细
     * @param color     颜色代码
     * @return bitmap
     */
    fun filletBitmap(
        bitmap: Bitmap,
        rx: Float,
        ry: Float,
        bl: Boolean,
        edge: Float = 0f,
        color: Int
    ): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        val color0 = if (color == 0) {
            -0x15db8 //默认橘黄色
        } else {
            color
        }
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output) //创建画布
        val paint = Paint()
        paint.isAntiAlias = true
        if (bl) {
            paint.color = color0
            paint.style = Paint.Style.STROKE //描边
            paint.strokeWidth = edge
        }
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        canvas.drawRoundRect(rectF, rx, ry, paint) //绘制圆角矩形
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) //取相交裁剪
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap 位图
     * @param region 倒影区域 0.1~1
     * @return bitmap
     */
    fun invertedBitmap(bitmap: Bitmap, region: Float): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0 || region <= 0f || region > 1f) {
            return bitmap
        }
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        matrix.preScale(1f, -1f) //镜像缩放
        val reflectionBitmap = Bitmap.createBitmap(
            bitmap, 0, (height * (1 - region)).toInt() //从哪个点开始绘制
            , width, (height * region).toInt() //绘制多高
            , matrix, false
        )
        val reflectionWithBitmap = Bitmap.createBitmap(
            width, height + (height * region).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(reflectionWithBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawBitmap(reflectionBitmap, 0f, height.toFloat(), null)
        val shader = LinearGradient(
            0F, bitmap.height.toFloat(), 0F,
            reflectionWithBitmap.height.toFloat(), 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP
        )
        val paint = Paint()
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN) //取两层绘制交集。显示下层。
        canvas.drawRect(
            0f,
            height.toFloat(),
            width.toFloat(),
            reflectionWithBitmap.height.toFloat(),
            paint
        )
        return reflectionWithBitmap
    }

    /**
     * 居中按比例裁剪图片
     *
     * @param bitmap 位图
     * @param wScale 裁剪宽 0~100%
     * @param hScale 裁剪高 0~100%
     * @return bitmap
     */
    fun cropBitmapOfPercentage2Center(bitmap: Bitmap, wScale: Float, hScale: Float): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0 ||
            wScale <= 0f || wScale >= 1f ||
            hScale <= 0f || hScale >= 1f
        ) {
            return bitmap
        }
        val wh = (bitmap.width * wScale).toInt()
        val hw = (bitmap.height * hScale).toInt()
        val retX = (bitmap.width * (1 - wScale) / 2).toInt()
        val retY = (bitmap.height * (1 - hScale) / 2).toInt()
        return Bitmap.createBitmap(bitmap, retX, retY, wh, hw, null, true)
    }

    /**
     * 左上角按比例裁剪图片
     *
     * @param bitmap 位图
     * @param wScale 裁剪宽 0~100%
     * @param hScale 裁剪高 0~100%
     * @return bitmap
     */
    fun cropBitmapOfPercentage2LeftTop(bitmap: Bitmap, wScale: Float, hScale: Float): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0 ||
            wScale <= 0f || wScale >= 1f ||
            hScale <= 0f || hScale >= 1f
        ) {
            return bitmap
        }
        val wh = (bitmap.width * wScale).toInt()
        val hw = (bitmap.height * hScale).toInt()
        return Bitmap.createBitmap(bitmap, 0, 0, wh, hw, null, true)
    }

    /**
     * 居中按比例裁剪图片
     *
     * @param bitmap 位图
     * @param cropW
     * @param cropH
     * @return bitmap
     */
    fun cropBitmapOfWH2Center(bitmap: Bitmap, cropW: Int, cropH: Int): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0 ||
            bitmap.width <= cropW || bitmap.height <= cropH ||
            cropW <= 0 || cropH <= 0
        ) {
            return bitmap
        }
        val retX = (bitmap.width - cropW) / 2
        val retY = (bitmap.height - cropH) / 2
        return Bitmap.createBitmap(bitmap, retX, retY, cropW, cropH, null, true)
    }

    /**
     * 按比例裁剪图片
     *
     * @param bitmap 位图
     * @param cropW
     * @param cropH
     * @return bitmap
     */
    fun cropBitmapOfWH2LeftTop(
        bitmap: Bitmap,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0
    ): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0 ||
            bitmap.width <= cropW || bitmap.height <= cropH ||
            cropW <= 0 || cropH <= 0
        ) {
            return bitmap
        }
        var x = offX
        var y = offY
        if (offX < 0) {
            x = 0
        }
        if (offY < 0) {
            y = 0
        }
        return Bitmap.createBitmap(bitmap, x, y, cropW, cropH, null, true)
    }

    /**
     * 高级图片质量压缩
     * @param bitmap  位图
     * @param maxSizeKB 压缩后的大小，单位kb
     */
    fun scaleMaxSize(bitmap: Bitmap, maxSizeKB: Int): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        var quality = 100
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        val baos = ByteArrayOutputStream()
        do {
            // 格式、质量、输出流
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos)
            val b = baos.toByteArray()
            // 将字节换成KB
            val bitmapSizeKB = b.size / 1024
            // 获取bitmap大小 是允许最大大小的多少倍
            if (bitmapSizeKB <= maxSizeKB) {
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, null, true)
            }
            quality -= 10
        } while (bitmapSizeKB > maxSizeKB)
        return bitmap
    }

    /**
     * 图片质量压缩
     *
     * @param bitmap
     * @param percentage   百分比 0~100%
     * @return Bitmap?
     */
    fun scaleQuality(bitmap: Bitmap, percentage: Float): Bitmap? {
        if (bitmap.width <= 0 || bitmap.height <= 0 || percentage <= 0) {
            return null
        }
        var quality = (percentage * 100).toInt()
        if (quality > 100) {
            quality = 100
        }
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val isBm = ByteArrayInputStream(baos.toByteArray())
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /***
     * 放大缩小图片
     * @param bitmap Bitmap 位图
     * @param newW Double 新的宽度
     * @param newH Double 新的高度
     * @param degrees Float 旋转角度
     * @return Bitmap
     */
    fun scaleSize(bitmap: Bitmap, newW: Double, newH: Double, degrees: Float = 0f): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0 || newW <= 0.0 || newH <= 0.0) {
            return bitmap
        }
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val scaleWidth = (newW / width).toFloat()
        val scaleHeight = (newH / height).toFloat()
        matrix.postScale(scaleWidth, scaleHeight)
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * 旋转图片
     *
     * @param bitmap Bitmap 要旋转的图片
     * @param degrees Float 旋转角度
     * @return bitmap
     */
    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        val matrix = Matrix()
        matrix.postRotate(degrees)
        val width = bitmap.width
        val height = bitmap.height
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * 图片合成
     *
     * @param backgroundBitmap 背景位图
     * @param foregroundBitmap 前景位图
     * @param offX X偏移
     * @param offY Y偏移
     * @param degrees 旋转角度
     * @return Bitmap
     */
    fun merge(
        backgroundBitmap: Bitmap,
        foregroundBitmap: Bitmap,
        offX: Float = 0f,
        offY: Float = 0f,
        degrees: Float = 0f
    ): Bitmap {
        if (backgroundBitmap.width <= 0 || backgroundBitmap.height <= 0 || foregroundBitmap.width <= 0 || foregroundBitmap.height <= 0) {
            return backgroundBitmap
        }
        val newbitmap = Bitmap.createBitmap(
            backgroundBitmap.width,
            backgroundBitmap.height,
            backgroundBitmap.config
        ) // 创建一个长宽一样的位图
        val cv = Canvas(newbitmap)
        cv.drawBitmap(backgroundBitmap, 0f, 0f, null) // 背景图
        cv.save() // 保存
        cv.rotate(degrees, foregroundBitmap.width / 2f, foregroundBitmap.height / 2f)
        cv.drawBitmap(foregroundBitmap, offX, offY, null)
        cv.restore() // 存储
        return newbitmap
    }


    /**
     * bitmap保存到指定路径
     * @param filePath String
     * @param bitmap Bitmap
     * @param bitmapType CompressFormat
     * @return Boolean
     */
    fun saveBitmap(
        filePath: String,
        bitmap: Bitmap,
        bitmapType: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Boolean {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return false
        }
        val file = File(filePath)
        if (file.isDirectory) {
            return false
        }
        if (file.isFile && file.exists()) {
            file.delete()
        }
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        val outputStream = FileOutputStream(file)
        try {
            bitmap.compress(bitmapType, 100, outputStream)
            outputStream.flush()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }
    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }


}