package com.abcnv.nvone.lib_framework

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import com.abcnv.nvone.lib_framework.glide.NvGlide
import java.io.*
import kotlin.math.max


object NvBitmapUtil {
    /**
     * 获取图片宽高，不占用内存
     * @param path String
     * @return Triple<Int, Int, Bitmap.Config?>
     */
    fun getBitmapWHOfNotOccupyingMemory(path: String): Triple<Int, Int, Bitmap.Config?> {
        val opts = BitmapFactory.Options()
        //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, opts)
        val imageWidth: Int = opts.outWidth
        val imageHeight: Int = opts.outHeight
        opts.inJustDecodeBounds = false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Triple<Int, Int, Bitmap.Config?>(imageWidth, imageHeight, opts.outConfig)
        } else {
            Triple<Int, Int, Bitmap.Config?>(imageWidth, imageHeight, null)
        }
    }

    /**
     * 圆形图片
     * @param bitmap Bitmap
     * @param strokeWidth Float
     * @param bl Boolean
     * @param edge Float
     * @param color Int
     * @param context Context?
     * @return Bitmap?
     */
    fun circleBitmap(
        bitmap: Bitmap,
        strokeWidth: Float,
        bl: Boolean,
        edge: Float = 0f,
        color: Int,
        context: Context? = null
    ): Bitmap? {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        val color0 = if (color == 0) -0x15db8 else color
        val diameter = Math.min(bitmap.width, bitmap.height)
        val newbitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newbitmap) //创建画布
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
        if (context == null || newbitmap == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, newbitmap.width, newbitmap.height)
    }

    /**
     * 圆角图片
     * @param bitmap Bitmap
     * @param rx Float  x方向上的圆角半径
     * @param ry Float  y方向上的圆角半径
     * @param bl Boolean    是否需要描边
     * @param edge Float    画笔粗细
     * @param color Int 颜色代码
     * @param context Context?
     * @return Bitmap?
     */
    fun filletBitmap(
        bitmap: Bitmap,
        rx: Float,
        ry: Float,
        bl: Boolean,
        edge: Float = 0f,
        color: Int,
        context: Context? = null
    ): Bitmap? {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        val color0 = if (color == 0) -0x15db8 else color
        val newbitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newbitmap) //创建画布
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
        if (context == null || newbitmap == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, newbitmap.width, newbitmap.height)
    }

    /**
     * 获得带倒影的图片方法
     * @param bitmap Bitmap
     * @param region Float   倒影区域 0.1~1
     * @param context Context?
     * @return Bitmap?
     */
    fun invertedBitmap(bitmap: Bitmap, region: Float, context: Context? = null): Bitmap? {
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
        if (context == null || reflectionWithBitmap == null) {
            return reflectionWithBitmap
        }
        return NvGlide.getBitmapOfContext(
            context,
            reflectionWithBitmap,
            reflectionWithBitmap.width,
            reflectionWithBitmap.height
        )
    }

    /**
     * 高级图片质量压缩
     * @param bitmap Bitmap
     * @param maxSizeKB Int 压缩后的大小，单位kb
     * @param context Context?
     * @return Bitmap?
     */
    fun scaleMaxSize(bitmap: Bitmap, maxSizeKB: Int, context: Context? = null): Bitmap? {
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
                val newbitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, null, true)
                if (context == null || newbitmap == null) {
                    return newbitmap
                }
                return NvGlide.getBitmapOfContext(
                    context,
                    newbitmap,
                    newbitmap.width,
                    newbitmap.height
                )
            }
            quality -= 10
            if (quality < 10) {
                return bitmap
            }
        } while (bitmapSizeKB > maxSizeKB)
        return bitmap
    }

    /**
     * 图片质量压缩
     * @param bitmap Bitmap
     * @param percentage Float  百分比 0~100%
     * @param context Context?
     * @return Bitmap?
     */
    fun scaleQuality(bitmap: Bitmap, percentage: Float, context: Context? = null): Bitmap? {
        if (bitmap.width <= 0 || bitmap.height <= 0 || percentage <= 0) {
            return null
        }
        var quality = (percentage * 100).toInt()
        if (quality > 100) {
            quality = 100
        }
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos)
        val isBm = ByteArrayInputStream(baos.toByteArray())
        val newbitmap = BitmapFactory.decodeStream(isBm, null, null)
        if (context == null || newbitmap == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, newbitmap.width, newbitmap.height)
    }

    /***
     * 放大缩小图片
     * @param bitmap Bitmap
     * @param newW Float 新的宽度
     * @param newH Float 新的高度
     * @param degrees Float 旋转角度
     * @param context Context?
     * @return Bitmap?
     */
    fun scaleSize(
        bitmap: Bitmap,
        newW: Float,
        newH: Float,
        degrees: Float = 0f,
        context: Context? = null
    ): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= 0 || height <= 0 || newW <= 0.0 || newH <= 0.0) {
            return bitmap
        }
        val matrix = Matrix()
        val scaleWidth = newW / width
        val scaleHeight = newH / height
        matrix.postScale(scaleWidth, scaleHeight)
        matrix.postRotate(degrees)
        val newbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        if (context == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, newbitmap.width, newbitmap.height)
    }

    /**
     * 旋转图片
     * @param bitmap Bitmap
     * @param degrees Float 旋转角度
     * @param context Context?
     * @return Bitmap?
     */
    fun rotate(bitmap: Bitmap, degrees: Float, context: Context? = null): Bitmap? {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return bitmap
        }
        val matrix = Matrix()
        matrix.postRotate(degrees)
        val width = bitmap.width
        val height = bitmap.height
        val newbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        if (context == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, newbitmap.width, newbitmap.height)
    }

    /**
     * 图片合成
     * @param backgroundBitmap Bitmap   背景位图
     * @param foregroundBitmap Bitmap   前景位图
     * @param offX Float    X偏移
     * @param offY Float    Y偏移
     * @param degrees Float 旋转角度
     * @param context Context?
     * @return Bitmap?
     */
    fun merge(
        backgroundBitmap: Bitmap,
        foregroundBitmap: Bitmap,
        offX: Float = 0f,
        offY: Float = 0f,
        degrees: Float = 0f,
        context: Context? = null
    ): Bitmap? {
        val b_w = backgroundBitmap.width
        val b_h = backgroundBitmap.height
        val f_w = backgroundBitmap.width
        val f_h = backgroundBitmap.height
        if (b_w <= 0 || b_h <= 0 || f_w <= 0 || f_h <= 0) {
            return backgroundBitmap
        }
        val newbitmap = Bitmap.createBitmap(b_w, b_h, backgroundBitmap.config)
        val cv = Canvas(newbitmap)
        cv.drawBitmap(backgroundBitmap, 0f, 0f, null) // 背景图
        cv.save() // 保存
        cv.rotate(degrees, foregroundBitmap.width / 2f, foregroundBitmap.height / 2f)
        cv.drawBitmap(foregroundBitmap, offX, offY, null)
        cv.restore() // 存储
        if (context == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, b_w, b_h)
    }


    /**
     * bitmap保存
     * @param path String
     * @param bitmap Bitmap
     * @param bitmapType CompressFormat
     * @return Boolean
     */
    fun saveBitmap(
        path: String,
        bitmap: Bitmap,
        bitmapType: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): Boolean {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return false
        }
        val file = File(path)
        if (file.isDirectory) {
            return false
        }
        if (file.isFile && file.exists()) {
            file.delete()
        }
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(file)
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
     * 创建缩略图
     * @param bitmap Bitmap
     * @param width Int
     * @param height Int
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfThumbnail(
        bitmap: Bitmap,
        width: Int = 40,
        height: Int = 40,
        context: Context? = null
    ): Bitmap? {
        val newbitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height)
        if (context == null) {
            return newbitmap
        }
        return NvGlide.getBitmapOfContext(context, newbitmap, newbitmap.width, newbitmap.height)
    }

    /**
     * InputStream转bitmap
     * @param input InputStream
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfInputStream(input: InputStream, context: Context? = null): Bitmap? {
        val bitmap = BitmapFactory.decodeStream(input)
        if (context == null || bitmap == null) {
            return bitmap
        }
        return NvGlide.getBitmapOfContext(context, bitmap, bitmap.width, bitmap.height)
    }

    /**
     * drawable转Bitmap
     * @param drawable Drawable
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfDrawable(drawable: Drawable, context: Context? = null): Bitmap? {
        val bitmap = drawable.toBitmap()
        if (context == null) {
            return bitmap
        }
        return NvGlide.getBitmapOfContext(context, bitmap, bitmap.width, bitmap.height)
    }

    /**
     * byte[]转bitmap
     * @param bs ByteArray
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfBytes(bs: ByteArray, context: Context? = null): Bitmap? {
        val bitmap = BitmapFactory.decodeByteArray(bs, 0, bs.size)
        if (context == null || bitmap == null) {
            return bitmap
        }
        return NvGlide.getBitmapOfContext(context, bitmap, bitmap.width, bitmap.height)
    }

    /**
     * 窗口截个屏
     * @param activity Activity
     * @return Bitmap?
     */
    fun getBitmapOfCaptureScreen(activity: Activity): Bitmap? {
        val decorView = activity.window.decorView
        //获取屏幕整张图片
        val bitmap = decorView.drawToBitmap()
        return NvGlide.getBitmapOfContext(activity, bitmap, bitmap.width, bitmap.height)
    }

    /**
     * YUV视频流格式转bitmap
     * @param byteArray ByteArray   YUV视频流格式
     * @param width Int
     * @param height Int
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfYUV(
        byteArray: ByteArray,
        width: Int,
        height: Int,
        context: Context? = null
    ): Bitmap? {
        if (byteArray.size <= 0 || width <= 0 || height <= 0) {
            return null
        }
        val yuvimage = YuvImage(byteArray, ImageFormat.NV21, width, height, null)
        //data是onPreviewFrame参数提供
        val baos = ByteArrayOutputStream()
        val rect = Rect(0, 0, yuvimage.width, yuvimage.height)
        yuvimage.compressToJpeg(rect, 100, baos)
        // 80--JPG图片的质量[0-100],100最高
        val rawImage = baos.toByteArray()
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.size, options)
        try {
            baos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (context == null) {
            return bitmap
        } else {
            return NvGlide.getBitmapOfContext(context, bitmap, width, height)
        }
    }

    /**
     * 获取ImageView中的Bitmap
     * @param view ImageView
     * @return Bitmap?
     */
    fun getBitmapOfImageView(view: ImageView): Bitmap? {
        return NvGlide.getBitmapOfContext(
            view.context,
            view.drawToBitmap(),
            view.drawToBitmap().width,
            view.drawToBitmap().height
        )
    }

    /**
     * view转bitmap
     * @param view View
     * @return Bitmap?
     */
    fun getBitmapOfView(view: View): Bitmap? {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        return if (view.width > 0 && view.height > 0) {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            NvGlide.getBitmapOfContext(view.context, bitmap, bitmap.width, bitmap.height)
        } else
            null
    }

    /**
     * 图片资源文件转bitmap
     * @param context Context
     * @param resId Int
     * @return Bitmap?
     */
    fun getBitmapOfResources(context: Context, resId: Int): Bitmap? {
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
        return NvGlide.getBitmapOfContext(context, bitmap, bitmap.width, bitmap.height)
    }

    /**
     * 将图片路径转Bitmap
     * @param path String
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfPath(path: String, context: Context? = null): Bitmap? {
        if (context == null) {
            return BitmapFactory.decodeFile(path)
        }
        val (w, h, config) = getBitmapWHOfNotOccupyingMemory(path)
        return NvGlide.getBitmapOfContext(context, File(path), w, h)
    }


    /**
     * 根据图片路径 获取图片 最大宽高不超过maxWidthHeight
     * @param filePath String
     * @param maxWidthHeight Int
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfMaxWidthHeight(
        filePath: String,
        maxWidthHeight: Int = 150,
        context: Context? = null
    ): Bitmap? {
        var wh = maxWidthHeight
        if (wh < 1) {
            wh = 150
        }
        //对于图片的二次采样,主要得到图片的宽与高
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        options.inJustDecodeBounds = false
        val max = max(imageWidth, imageHeight)
        var inSampleSize = 1
        if (max > wh) {
            inSampleSize = max / wh
        }
        //并且制定缩放比例
        options.inSampleSize = inSampleSize
        if (context == null) {
            return BitmapFactory.decodeFile(filePath, options)
        }
        return NvGlide.getBitmapOfContext(
            context,
            File(filePath),
            imageWidth / inSampleSize,
            imageHeight / inSampleSize
        )
    }

    /**
     * 根据图片路径 获取图片
     * @param path String
     * @param sampleSize Int
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfSampleSize(
        path: String,
        sampleSize: Int = 1,
        context: Context? = null
    ): Bitmap? {
        val (w, h, config) = getBitmapWHOfNotOccupyingMemory(path)
        if (context == null) {
            //对于图片的二次采样,主要得到图片的宽与高
            val options = BitmapFactory.Options()
            val inSampleSize = sampleSize
            options.inSampleSize = inSampleSize
            return BitmapFactory.decodeFile(path, options)
        }
        return NvGlide.getBitmapOfContext(
            context,
            File(path),
            w / sampleSize,
            h / sampleSize
        )
    }

    /**
     * 根据图片路径 获取图片
     * @param path String
     * @param newW Int
     * @param newH Int
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfWidthHeight(
        path: String,
        newW: Int,
        newH: Int,
        context: Context? = null
    ): Bitmap? {
        if (context == null) {
            var bitmap = BitmapFactory.decodeFile(path)
            bitmap = scaleSize(bitmap, newW + 0f, newH + 0f, 0f)
            return bitmap
        }
        return NvGlide.getBitmapOfContext(context, File(path), newW, newH)
    }

    /**
     * 按比例裁剪图片
     * @param bitmap Bitmap
     * @param cropW Int
     * @param cropH Int
     * @param offX Int
     * @param offY Int
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfCropWH2LeftTop(
        bitmap: Bitmap, cropW: Int, cropH: Int,
        offX: Int = 0, offY: Int = 0, context: Context? = null
    ): Bitmap? {
        if (bitmap.width != cropW + offX ||
            bitmap.height != cropH + offY ||
            offX < 0 ||
            offY < 0 ||
            cropW < 1 ||
            cropH < 1
        ) {
            return null
        }
        val result = Bitmap.createBitmap(bitmap, offX, offY, cropW, cropH, null, true)
        if (context == null || result == null) {
            return result
        }
        return NvGlide.getBitmapOfContext(context, result, result.width, result.height)


    }

    /**
     * 获取区块Bitmap
     * @param path String           jpg/png  不能是bmp...
     * @param imageBlockRect Rect   Rect 切块的区域
     * @param sampleSize Int
     * @return Bitmap?
     */
    fun getBitmapOfImageBlock(
        path: String,
        imageBlockRect: Rect,
        sampleSize: Int = 1
    ): Bitmap? {
        if (imageBlockRect.width() == 0 || imageBlockRect.height() == 0) {
            return null
        }
        val (imgWidth, imgHeight, outConfig) = getBitmapWHOfNotOccupyingMemory(path)
        if (imageBlockRect.left >= imgWidth - 1 || imageBlockRect.top >= imgHeight - 1) {
            return null
        }

        var regionDecoder: BitmapRegionDecoder? = null
        try {
            regionDecoder = BitmapRegionDecoder.newInstance(path, false)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = outConfig
            options.inSampleSize = sampleSize
            val bitmap = regionDecoder.decodeRegion(imageBlockRect, options)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                regionDecoder?.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 重新编码Bitmap
     * @param src Bitmap
     * @param format CompressFormat 编码后的格式（目前只支持png和jpeg这两种格式）
     * @param quality Int   重新生成后的bitmap的质量
     * @param context Context?
     * @return Bitmap?
     */
    fun getBitmapOfFormat(
        src: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100,
        context: Context? = null
    ): Bitmap? {
        val baos = ByteArrayOutputStream()
        src.compress(format, quality, baos)
        val array = baos.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
        if (context == null || bitmap == null) {
            return bitmap
        }
        return NvGlide.getBitmapOfContext(context, bitmap, bitmap.width, bitmap.height)
    }

    /**
     * 图片格式转成png
     * @param path String
     * @return String
     */
    fun img2png(path: String): String {
        val srcFile = File(path)
        val copyFile = File(path + ".png")
        val result = NvFileUtil.copyFile(srcFile, copyFile)
        if (!result) {
            return path
        }
        val baos = ByteArrayOutputStream()
        var fos: FileOutputStream? = null
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeFile(copyFile.absolutePath)
            if (bitmap == null) {
                return path
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            fos = FileOutputStream(path)
            fos.write(baos.toByteArray())
            fos.flush()
            return path
        } catch (e: Exception) {
            e.printStackTrace()
            return path
        } finally {
            bitmap?.recycle()
            try {
                baos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                fos?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /**
     * bitmap转 byte[]
     * @param bitmap Bitmap
     * @param cf CompressFormat
     * @return ByteArray
     */
    fun bitmap2Bytes(
        bitmap: Bitmap,
        cf: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): ByteArray {
        if (bitmap.width == 0 || bitmap.height == 0) {
            return ByteArray(0)
        }
        val outStream = ByteArrayOutputStream()
        bitmap.compress(cf, 100, outStream)
        val bytes = outStream.toByteArray()
        try {
            outStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bytes
    }

    /**
     * 将图片转换成Base64编码的字符串 ,图片的base64编码是不包含图片头的，如（data:image/jpg;base64,）
     * bitmap转换成Base64编码的字符串
     *
     * @param bitmap Bitmap
     * @return String
     */
    fun bitmap2Base64(bitmap: Bitmap): String {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return ""
        }
        var outputStream: OutputStream? = null
        try {
            outputStream = ByteArrayOutputStream()
            if (bitmap.isRecycled) return ""
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    /**
     * 将图片转换成Base64编码的字符串 ,图片的base64编码是不包含图片头的，如（data:image/jpg;base64,）
     * 图片文件转换成Base64编码的字符串
     *
     * @param path String
     * @return String
     */
    fun imgFile2Base64(path: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(path)
            //创建一个字符流大小的数组。
            val data = ByteArray(inputStream.available())
            //写入数组
            inputStream.read(data)
            //用默认的编码格式进行编码
            return Base64.encodeToString(data, Base64.DEFAULT) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    /**
     * 读取照片exif信息中的旋转角度
     * @param path String
     * @return Int
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

    /**
     * 获取系统图片Path
     * @param context Context
     * @param data Uri
     * @return String
     */
    fun getSystemPhotoPath(context: Context, data: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        var picturePath = ""
        context.contentResolver.query(data, filePathColumn, null, null, null)?.let {
            try {
                it.moveToFirst()
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                //picturePath就是图片在储存卡所在的位置
                picturePath = it.getString(columnIndex)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                it.close()
            }
        }
        return picturePath
    }

    /**
     * 获取系统相册
     * @param context Context
     * @return MutableList<String>
     */
    fun getSystemPhotoList(context: Context): MutableList<String> {
        val allList = mutableListOf<String>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentResolver = context.contentResolver
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} desc"
        val cursor = contentResolver.query(uri, null, null, null, sortOrder)
        cursor?.let {
            while (it.moveToNext()) {
                val index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path = it.getString(index)
                allList.add(path)
            }
        }
        cursor?.close()
        return allList
    }

    /**
     * 图片转灰度
     * @param bitmap Bitmap
     * @param context Context?
     * @return Bitmap?
     */
    fun convertGreyImg(bitmap: Bitmap, context: Context? = null): Bitmap? {
        val w = bitmap.width
        val h = bitmap.height
        val ps = IntArray(w * h)
        bitmap.getPixels(ps, 0, w, 0, 0, w, h)
        val alpha = 0xFF shl 24

        for (i in 0..h - 1) {
            for (j in 0..w - 1) {
                var grey = ps[w * i + j]
                val red = grey and 0x00FF0000 shr 16
                val green = grey and 0x0000FF00 shr 8
                val blue = grey and 0x000000FF

                val r = red.toFloat() * 0.3
                val g = green.toFloat() * 0.59
                val b = blue.toFloat() * 0.11

                grey = (r + g + b).toInt()
                grey = alpha or (grey shl 16) or (grey shl 8) or grey
                ps[w * i + j] = grey
            }
        }

        val result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        result.setPixels(ps, 0, w, 0, 0, w, h)
        if (context == null || result == null) {
            return result
        }
        return NvGlide.getBitmapOfContext(context, result, result.width, result.height)

    }

}