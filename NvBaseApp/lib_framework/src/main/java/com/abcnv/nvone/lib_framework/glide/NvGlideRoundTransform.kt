package com.abcnv.nvone.lib_framework.glide

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @Description:
 *
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class NvGlideRoundTransform : BitmapTransformation {
    var mLeftTopRadius = 0f
    var mLeftBottomRadius = 0f
    var mRightTopRadius = 0f
    var mRightBottomRadius = 0f

    var radius = mutableListOf<Float>()

    constructor(
        leftTopRadius: Float,
        leftBottomRadius: Float,
        rightTopRadius: Float,
        rightBottomRadius: Float
    ) {
        mLeftTopRadius = leftTopRadius
        mLeftBottomRadius = leftBottomRadius
        mRightTopRadius = rightTopRadius
        mRightBottomRadius = rightBottomRadius

        radius.add(mLeftTopRadius)
        radius.add(mLeftBottomRadius)
        radius.add(mRightTopRadius)
        radius.add(mRightBottomRadius)
        radius.add(mRightTopRadius)
        radius.add(mRightBottomRadius)
        radius.add(mLeftTopRadius)
        radius.add(mLeftBottomRadius)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        val w = toTransform.getWidth()
        val h = toTransform.getHeight()

        var result = pool[w, h, Bitmap.Config.ARGB_8888]
        if (null == result) {
            result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result)
        val paint = Paint()
        val shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.setShader(shader)
        paint.setAntiAlias(true)
        val rectF = RectF(0f, 0f, w + 0f, h + 0f)
        val path = Path()
        path.addRoundRect(rectF, radius.toFloatArray(), Path.Direction.CW)
        canvas.clipPath(path)
        canvas.drawRect(rectF, paint)
        return result
    }
}