package com.abcnv.nvone.mediaprojectionapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.SystemClock
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.abcnv.nvone.mediaprojectionapp.R
import java.nio.ByteBuffer
import java.util.*

object NvMediaProjectionUtil {
    fun launch(pair: Pair<ActivityResultLauncher<Intent>, Intent>?) {
        pair?.first?.launch(pair?.second)
    }

    /**
     * 获取屏幕录制的权限
     */
    fun getMediaProjectionToOnCreate(
        activity: AppCompatActivity,
        callback: (intent: Intent?) -> Unit
    ): Pair<ActivityResultLauncher<Intent>, Intent> {
        val mediaProjectionManager = activity.getSystemService(MediaProjectionManager::class.java)
        val intent0 = mediaProjectionManager.createScreenCaptureIntent()
        val startActivityForResult = ActivityResultContracts.StartActivityForResult()
        val activityResultLauncher = activity.registerForActivityResult(startActivityForResult) {
            Log.d("0011==", "mp--002")
            val data = it.data
            if (it.resultCode == Activity.RESULT_OK && data != null) {
                callback.invoke(data)
            } else {
                callback.invoke(null)
            }
        }
        return Pair(activityResultLauncher, intent0)
    }


    fun getMediaProjection(context: Context, resultCode: Int, resultData: Intent): MediaProjection {
        val mediaProjectionManager = context.getSystemService(MediaProjectionManager::class.java)
        return mediaProjectionManager.getMediaProjection(resultCode, resultData)
    }

    fun getNotification(context: Context, channelId: String, channelName: String): Notification {

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notification_service =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notification_service.createNotificationChannel(channel)

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.app_name))
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.getResources(),
                    R.mipmap.ic_launcher
                )
            ).build()
    }


    fun startCapture(mediaProjection: MediaProjection, width: Int, height: Int): Bitmap {
        Objects.requireNonNull(mediaProjection)
        @SuppressLint("WrongConstant")
        val imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 60)
        val virtualDisplay = mediaProjection.createVirtualDisplay(
            "screen",
            width,
            height,
            1,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.getSurface(),
            null,
            null
        )
        //取现在最新的图片
        SystemClock.sleep(1000)
        //取最新的图片
        val image: Image = imageReader.acquireLatestImage()
        //        Image image = imageReader.acquireNextImage();
        //释放 virtualDisplay,不释放会报错
        virtualDisplay.release()
        val bitmap = image2Bitmap(image)
        //记得关闭 image
        try {
            image.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun image2Bitmap(image: Image): Bitmap {
        //获取捕获的照片数据
        val width = image.width
        val height = image.height
        //拿到所有的 Plane 数组
        val plane = image.planes[0]
        val buffer: ByteBuffer = plane.buffer
        //相邻像素样本之间的距离，因为RGBA，所以间距是4个字节
        val pixelStride = plane.pixelStride
        //每行的宽度
        val rowStride = plane.rowStride
        //因为内存对齐问题，每个buffer 宽度不同，所以通过pixelStride * width 得到大概的宽度，
        //然后通过 rowStride 去减，得到大概的内存偏移量，不过一般都是对齐的。
        val rowPadding = rowStride - pixelStride * width
        val w = width + rowPadding / pixelStride
        // 创建具体的bitmap大小，由于rowPadding是RGBA 4个通道的，所以也要除以pixelStride，得到实际的宽
        val bitmap = Bitmap.createBitmap(w, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }
}