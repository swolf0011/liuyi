package com.abcnv.nvone.lib_framework.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

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
object NvGlide {
    /**
     * 清除内存缓存:必须在UI线程中调用
     */
    fun clearMemory(context: Context) {
        Glide.get(context).clearMemory()
    }

    /**
     * 清除磁盘缓存:必须在后台线程中调用，建议同时clearMemory()
     */
    fun clearDiskCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    /**
     * 显示图片
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadOfContext(context: Context, iv: ImageView, t: Any) {
        val requestBuilder = Glide.with(context).load(t)
        requestBuilder.placeholder(NvGlideBuilder.placeholderImgRes) //设置占位图
            .error(NvGlideBuilder.errorImgRes) //设置错误图片
            .dontTransform().dontAnimate()//淡入淡出效果
            .into(iv)
    }

    /**
     * 显示图片
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadOfActivity(activity: FragmentActivity, iv: ImageView, t: Any) {
        val requestBuilder = Glide.with(activity).load(t)
        requestBuilder.placeholder(NvGlideBuilder.placeholderImgRes) //设置占位图
            .error(NvGlideBuilder.errorImgRes) //设置错误图片
            .dontTransform().dontAnimate()//淡入淡出效果
            .into(iv)
    }

    /**
     * 显示图片
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadOfFragment(fragment: Fragment, iv: ImageView, t: Any) {
        val requestBuilder = Glide.with(fragment).load(t)
        requestBuilder.placeholder(NvGlideBuilder.placeholderImgRes) //设置占位图
            .error(NvGlideBuilder.errorImgRes) //设置错误图片
            .dontTransform().dontAnimate()//淡入淡出效果
            .into(iv)
    }

    /**
     * 显示图片
     * @param t Any  :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun getBitmapOfContext(context: Context, t: Any, w: Int, h: Int): Bitmap? {
        try {
            val requestBuilder = Glide.with(context).asBitmap().load(t)
            return requestBuilder.submit(w, h).get()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 显示图片
     * @param t Any  :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun getBitmapOfFragmentActivity(activity: FragmentActivity, t: Any, w: Int, h: Int): Bitmap? {
        try {
            val requestBuilder = Glide.with(activity).asBitmap().load(t)
            return requestBuilder.submit(w, h).get()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 显示图片
     * @param t Any  :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun getBitmapOfFragment(fragment: Fragment, t: Any, w: Int, h: Int): Bitmap? {
        try {
            val requestBuilder = Glide.with(fragment).asBitmap().load(t)
            return requestBuilder.submit(w, h).get()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 显示裁剪的图片：从左上角0,0点切剪
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadOfCropContext(
        context: Context,
        t: Any,
        iv: ImageView,
        srcW: Int,
        srcH: Int,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0,
        progressBar: ProgressBar? = null
    ) {
        when (t) {
            is String, is Int, is Uri, is Bitmap, is ByteArray, is File -> {
                val requestBuilder = Glide.with(context).asBitmap().load(t)
                customTargetBitmap(
                    requestBuilder,
                    iv,
                    srcW,
                    srcH,
                    cropW,
                    cropH,
                    offX,
                    offY,
                    progressBar
                )
            }
            is Drawable -> {
                val requestBuilder = Glide.with(context).asDrawable().load(t)
                customTargetDrawable(
                    requestBuilder,
                    iv,
                    srcW,
                    srcH,
                    cropW,
                    cropH,
                    offX,
                    offY,
                    progressBar
                )
            }
        }
    }

    /**
     * 显示裁剪的图片：从左上角0,0点切剪
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadOfCropActivity(
        activity: FragmentActivity,
        t: Any,
        iv: ImageView,
        srcW: Int,
        srcH: Int,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0,
        progressBar: ProgressBar? = null
    ) {
        when (t) {
            is String, is Int, is Uri, is Bitmap, is ByteArray, is File -> {
                val requestBuilder = Glide.with(activity).asBitmap().load(t)
                customTargetBitmap(
                    requestBuilder,
                    iv,
                    srcW,
                    srcH,
                    cropW,
                    cropH,
                    offX,
                    offY,
                    progressBar
                )
            }
            is Drawable -> {
                val requestBuilder = Glide.with(activity).asDrawable().load(t)
                customTargetDrawable(
                    requestBuilder,
                    iv,
                    srcW,
                    srcH,
                    cropW,
                    cropH,
                    offX,
                    offY,
                    progressBar
                )
            }
        }
    }


    /**
     * 显示裁剪的图片：从左上角0,0点切剪
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadOfCropFragment(
        fragment: Fragment,
        t: Any,
        iv: ImageView,
        srcW: Int,
        srcH: Int,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0,
        progressBar: ProgressBar? = null
    ) {
        when (t) {
            is String, is Int, is Uri, is Bitmap, is ByteArray, is File -> {
                val requestBuilder = Glide.with(fragment).asBitmap().load(t)
                customTargetBitmap(
                    requestBuilder,
                    iv,
                    srcW,
                    srcH,
                    cropW,
                    cropH,
                    offX,
                    offY,
                    progressBar
                )
            }
            is Drawable -> {
                val requestBuilder = Glide.with(fragment).asDrawable().load(t)
                customTargetDrawable(
                    requestBuilder,
                    iv,
                    srcW,
                    srcH,
                    cropW,
                    cropH,
                    offX,
                    offY,
                    progressBar
                )
            }
        }
    }

    private fun customTargetBitmap(
        requestBuilder: RequestBuilder<Bitmap>,
        iv: ImageView,
        srcW: Int,
        srcH: Int,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0,
        progressBar: ProgressBar? = null,
    ) {
        val customTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmap = cropBitmapOfWH2LeftTop(resource, cropW, cropH, offX, offY)
                resource.recycle()
                iv.setImageBitmap(bitmap)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        }
        requestBuilder.placeholder(NvGlideBuilder.placeholderImgRes) //设置占位图
            .error(NvGlideBuilder.errorImgRes) //设置错误图片
            .override(srcW, srcH).addListener(setRequestListener<Bitmap>(progressBar))
            .dontAnimate()//淡入淡出效果
            .into(customTarget)
    }

    private fun customTargetDrawable(
        requestBuilder: RequestBuilder<Drawable>,
        iv: ImageView,
        srcW: Int,
        srcH: Int,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0,
        progressBar: ProgressBar? = null
    ) {
        val customTarget = object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val srcBitmap = resource.toBitmap(
                    resource.intrinsicWidth, resource.intrinsicHeight, Bitmap.Config.ARGB_8888
                )
                val bitmap = cropBitmapOfWH2LeftTop(srcBitmap, cropW, cropH, offX, offY)
                srcBitmap.recycle()
                iv.setImageBitmap(bitmap)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        }
        requestBuilder.placeholder(NvGlideBuilder.placeholderImgRes) //设置占位图
            .error(NvGlideBuilder.errorImgRes) //设置错误图片
            .override(srcW, srcH).addListener(setRequestListener<Drawable>(progressBar))
            .dontAnimate()//淡入淡出效果
            .into(customTarget)
    }

    /**
     * 显示不裁剪的图片,设置圆角：
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadRadiusContext(
        context: Context,
        iv: ImageView,
        t: Any,
        radius: Float = 0f,
        progressBar: ProgressBar? = null
    ) {
        loadRadiusContext(context, iv, t, radius, radius, radius, radius, progressBar)
    }

    /**
     * 显示不裁剪的图片,设置圆角：
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadRadiusContext(
        context: Context,
        iv: ImageView,
        t: Any,
        topLeftR: Float = 0f,
        topRightR: Float = 0f,
        bottomRightR: Float = 0f,
        bottomLeftR: Float = 0f,
        progressBar: ProgressBar? = null,
    ) {
        when (t) {
            is String, is Int, is Uri, is Bitmap, is ByteArray -> {
                val requestBuilder = Glide.with(context).asBitmap().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
            is Drawable -> {
                val requestBuilder = Glide.with(context).asDrawable().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
            is File -> {
                val requestBuilder = Glide.with(context).asFile().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
        }
    }

    /**
     * 显示不裁剪的图片,设置圆角：
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadRadiusActivity(
        activity: FragmentActivity,
        iv: ImageView,
        t: Any,
        radius: Float = 0f,
        progressBar: ProgressBar? = null
    ) {
        loadRadiusActivity(activity, iv, t, radius, radius, radius, radius, progressBar)
    }

    /**
     * 显示不裁剪的图片,设置圆角：
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadRadiusActivity(
        activity: FragmentActivity,
        iv: ImageView,
        t: Any,
        topLeftR: Float = 0f,
        topRightR: Float = 0f,
        bottomRightR: Float = 0f,
        bottomLeftR: Float = 0f,
        progressBar: ProgressBar? = null,
    ) {
        when (t) {
            is String, is Int, is Uri, is Bitmap, is ByteArray -> {
                val requestBuilder = Glide.with(activity).asBitmap().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
            is Drawable -> {
                val requestBuilder = Glide.with(activity).asDrawable().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
            is File -> {
                val requestBuilder = Glide.with(activity).asFile().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
        }
    }

    /**
     * 显示不裁剪的图片,设置圆角：
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadRadiusFragment(
        fragment: Fragment,
        iv: ImageView,
        t: Any,
        radius: Float = 0f,
        progressBar: ProgressBar? = null
    ) {
        loadRadiusFragment(fragment, iv, t, radius, radius, radius, radius, progressBar)
    }

    /**
     * 显示不裁剪的图片,设置圆角：
     * @param t Any :httpUrl(String)/resId(Int)/File/Uri/Bitmap/Drawable/ByteArray
     */
    fun loadRadiusFragment(
        fragment: Fragment,
        iv: ImageView,
        t: Any,
        topLeftR: Float = 0f,
        topRightR: Float = 0f,
        bottomRightR: Float = 0f,
        bottomLeftR: Float = 0f,
        progressBar: ProgressBar? = null,
    ) {
        when (t) {
            is String, is Int, is Uri, is Bitmap, is ByteArray -> {
                val requestBuilder = Glide.with(fragment).asBitmap().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
            is Drawable -> {
                val requestBuilder = Glide.with(fragment).asDrawable().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
            is File -> {
                val requestBuilder = Glide.with(fragment).asFile().load(t)
                intoImageView(
                    requestBuilder,
                    iv,
                    topLeftR,
                    topRightR,
                    bottomRightR,
                    bottomLeftR,
                    progressBar
                )
            }
        }
    }

    private fun <T> intoImageView(
        requestBuilder: RequestBuilder<T>,
        iv: ImageView,
        topLeftR: Float = 0f,
        topRightR: Float = 0f,
        bottomRightR: Float = 0f,
        bottomLeftR: Float = 0f,
        progressBar: ProgressBar? = null,
    ) {
        val transform = MultiTransformation(
            CenterCrop(),
            GranularRoundedCorners(topLeftR, topRightR, bottomRightR, bottomLeftR)
        )
        requestBuilder.placeholder(NvGlideBuilder.placeholderImgRes) //设置占位图
            .error(NvGlideBuilder.errorImgRes) //设置错误图片
            .addListener(setRequestListener<T>(progressBar))
            .transform(transform)//自定义转换
            .dontAnimate()//淡入淡出效果
            .into(iv)
    }


    /**
     * 创建Bitmap请求监听
     * @param progressBar ProgressBar?
     * @return RequestListener<T>  Bitmap, Drawable, File,
     */
    private fun <T> setRequestListener(progressBar: ProgressBar?): RequestListener<T> {
        val requestListener = object : RequestListener<T> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<T>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: T?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<T>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }
        }
        return requestListener
    }

    /**
     * 按比例裁剪图片
     * @param bitmap 位图
     * @return bitmap
     */
    fun cropBitmapOfWH2LeftTop(
        bitmap: Bitmap,
        cropW: Int,
        cropH: Int,
        offX: Int = 0,
        offY: Int = 0
    ): Bitmap {
        if (bitmap.width != cropW + offX || bitmap.height != cropH + offY ||
            offX < 0 || offY < 0 || cropW < 1 || cropH < 1
        ) {
            return bitmap.copy(bitmap.config, true)
        }
        return Bitmap.createBitmap(bitmap, offX, offY, cropW, cropH, null, true)
    }
}