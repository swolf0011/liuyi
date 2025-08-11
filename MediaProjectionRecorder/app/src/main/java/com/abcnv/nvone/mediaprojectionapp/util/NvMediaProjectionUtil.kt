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
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.abcnv.nvone.lib_util.NvFileUtil
import com.abcnv.nvone.mediaprojectionapp.R
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
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
            val data = it.data
            if (it.resultCode == Activity.RESULT_OK && data != null) {
                callback.invoke(data)
            } else {
                callback.invoke(null)
            }
        }
        return Pair(activityResultLauncher, intent0)
    }

    /**
     * 获取屏幕录制的权限
     */
    fun initMediaProjection(
        activity: AppCompatActivity,
        callbackSuccess: (intent: Intent) -> Unit,
        callbackFail: () -> Unit
    ): Pair<ActivityResultLauncher<Intent>, Intent> {
        val mediaProjectionManager = activity.getSystemService(MediaProjectionManager::class.java)
        val intent0 = mediaProjectionManager.createScreenCaptureIntent()
        val startActivityForResult = ActivityResultContracts.StartActivityForResult()
        val startMediaProjection = activity.registerForActivityResult(startActivityForResult) {
            val data = it.data
            if (it.resultCode == Activity.RESULT_OK && data != null) {
                callbackSuccess.invoke(data)
            } else {
                callbackFail.invoke()
            }
        }
        return Pair(startMediaProjection, intent0)
//        startMediaProjection.launch(intent0)
    }


    fun getMediaProjection(
        context: Context,
        resultCode: Int,
        resultData: Intent
    ): MediaProjection {
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
            "screenCapture",
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


    fun getPath(context:Context): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val curDate = Date(System.currentTimeMillis())
        val curTime: String = formatter.format(curDate).replace(" ", "")
        val dir = NvFileUtil.getExternalFilesDir(context)
        val path = "${dir.absolutePath}/HD_${curTime}qq.mp4"
        return path
    }

    fun initMediaRecorder(
        context: Context,
        path: String,
        width: Int,
        height: Int
    ): MediaRecorder? {
        val file = File(path)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        file.delete()
        val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
        try {
            //设置音频记录的音频源。
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            //设置视频记录的视频源。
            recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)

            //设置记录的媒体文件的输出转换格式。
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

            //设置视频编码格式，请注意这里使用默认，实际app项目需要考虑兼容问题，应该选择H264
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            //设置音频编码格式，请注意这里使用默认，实际app项目需要考虑兼容问题，应该选择AAC
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            //设置帧数 选择 30即可， 过大帧数也会让视频文件更大当然也会更流畅，但是没有多少实际提升。人眼极限也就30帧了。
            recorder.setVideoFrameRate(30)
            //视频宽高
            recorder.setVideoSize(width, height)
            //媒体文件输出路径。
            recorder.setOutputFile(file)
            //设置比特率 一般是 1*分辨率 到 10*分辨率 之间波动。比特率越大视频越清晰但是视频文件也越大。
            recorder.setVideoEncodingBitRate(5 * width * height)
//            recorder.setPreviewDisplay(surfaceHolder.getSurface());
            recorder.prepare()
//                val surface = Surface(mTextureView.getSurfaceTexture())
//                setPreviewDisplay(surface)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return recorder
    }

    fun startRecorderScreen(mediaRecorder: MediaRecorder):Boolean{
        try {

            mediaRecorder.start()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    fun initVirtualDisplay(
        mediaProjection: MediaProjection,
        surface: Surface,
        width: Int,
        height: Int
    ): VirtualDisplay {
        val virtualDisplay = mediaProjection.createVirtualDisplay(
            "screenCapture",
            width,
            height,
            1,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            surface,
            null,
            null
        )
        return virtualDisplay
    }


    fun stopRecorderScreen(
        mediaProjection: MediaProjection?,
        mediaRecorder: MediaRecorder?,
        virtualDisplay: VirtualDisplay?
    ) {
        virtualDisplay?.release()
        mediaRecorder?.setOnErrorListener(null)
        try {
            mediaRecorder?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaRecorder?.release()
        mediaProjection?.stop()
    }
}