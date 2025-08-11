package com.abcnv.one.lib_mediarecorder

import android.content.Context
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Build
import android.view.Surface

object MediaRecorderUtil {
    var mRecorder: MediaRecorder? = null

    fun startRecord(
        context: Context,
        camera: Camera,
        path: String,
        surface: Surface,
        callback: (Boolean) -> Unit
    ) {

        if (mRecorder == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                mRecorder = MediaRecorder(context)
            } else {
                mRecorder = MediaRecorder()
            }
        }
        if (mRecorder == null) {
            return
        }
        mRecorder?.let {
            camera.stopPreview()
            camera.unlock()
            it.setCamera(camera)

            try {
                // 设置音频采集方式
                it.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                //设置视频的采集方式
                it.setVideoSource(MediaRecorder.VideoSource.CAMERA)
                //设置文件的输出格式
                it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)//aac\_adif， aac\_adts， output\_format\_rtp\_avp， output\_format\_mpeg2ts ，webm
                //设置audio的编码格式
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                //设置video的编码格式
                it.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                //设置录制的视频编码比特率
                it.setVideoEncodingBitRate(1024 * 1024)
                //设置录制的视频帧率,注意文档的说明:
                it.setVideoFrameRate(30);
                //设置要捕获的视频的宽度和高度
//                mSurfaceHolder.setFixedSize(320, 240)//最高只能设置640x480
                it.setVideoSize(320, 240)//最高只能设置640x480
                //设置记录会话的最大持续时间（毫秒）
                it.setMaxDuration(60 * 1000)
                it.setPreviewDisplay(surface)
                //设置输出文件的路径
                it.setOutputFile(path)
                //准备录制
                it.prepare()
                //开始录制
                it.start()
                callback.invoke(true)
            } catch (e: Exception) {
                e.printStackTrace()
                callback.invoke(false)
            }
        }
    }

    fun stopRecord(callback: (Boolean) -> Unit){
        mRecorder?.let {
            try {
                it.stop()
                it.reset()
                callback.invoke(true)
            }catch (e:Exception){
                e.printStackTrace()
                callback.invoke(false)
            }
        }
    }
}