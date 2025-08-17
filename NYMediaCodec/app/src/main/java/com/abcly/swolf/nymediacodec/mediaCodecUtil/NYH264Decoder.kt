package com.abcly.swolf.nymediacodec.mediaCodecUtil

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.view.Surface
import java.io.IOException


/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/5/9 14:11
 */
class NYH264Decoder private constructor() {
    companion object {
        private var instance: NYH264Decoder? = null

        @Synchronized
        fun getInstance(): NYH264Decoder {
            if (instance == null) {
                instance = NYH264Decoder()
            }
            return instance!!
        }
    }

    private var mediaCodec: MediaCodec? = null
    private var mediaFormat: MediaFormat? = null
    private var mediaExtractor: MediaExtractor? = null
    private val mSpeedController = NYSpeedManager()
    private var isDecodeFinish = false
    private var thread: Thread? = null

    private fun decoderMP4Thread() {
        while (!isDecodeFinish && mediaCodec != null) {
            val inputIndex = mediaCodec!!.dequeueInputBuffer(-1)
            val byteBuffer = mediaCodec!!.getInputBuffer(inputIndex)
            if (byteBuffer != null && mediaExtractor != null) {
                //读取一片或者一帧数据
                val sampSize = mediaExtractor!!.readSampleData(byteBuffer, 0)
                //读取时间戳
                val time: Long = mediaExtractor!!.getSampleTime()
                if (sampSize > 0) {
                    mediaCodec!!.queueInputBuffer(inputIndex, 0, sampSize, time, 0)
                    //读取一帧后必须调用，提取下一帧
                    //控制帧率在30帧左右
                    mSpeedController.preRender(time)
                    mediaExtractor!!.advance()
                }
            }
            val bufferInfo = MediaCodec.BufferInfo()
            val outIndex = mediaCodec!!.dequeueOutputBuffer(bufferInfo, 0)
            if (outIndex >= 0) {
                mediaCodec!!.releaseOutputBuffer(outIndex, true)
            }
        }
    }

    fun close() {
        try {
            if (mediaCodec != null) {
                isDecodeFinish = true
                try {
                    if (thread != null) {
                        thread?.join(2000)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                thread?.isAlive()
                mediaCodec?.stop()
                mediaCodec?.release()
                mediaCodec = null
                mSpeedController.reset()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        instance = null
    }


    fun startMP4Decode(mp4_play_path: String, surface: Surface) {
        initMediaCodecSys(mp4_play_path, surface)
        if (mediaCodec != null && mediaExtractor != null) {
            thread = Thread(Runnable {
                decoderMP4Thread()
            })
            thread?.setName("DecoderMP4Thread")
            thread?.start()
        }
    }

    private fun initMediaCodecSys(mp4_play_path: String, surface: Surface) {
        try {
            mediaCodec = MediaCodec.createDecoderByType("video/avc")
            mediaFormat = MediaFormat.createVideoFormat("video/avc", 1280, 720)
            mediaExtractor = MediaExtractor()
            //MP4 文件存放位置
            mediaExtractor?.setDataSource(mp4_play_path)
            mediaExtractor?.let {
                for (i in 0 until it.getTrackCount()) {
                    val format: MediaFormat = it.getTrackFormat(i)
                    val mime = format.getString(MediaFormat.KEY_MIME)
                    if (mime!!.startsWith("video")) {
                        mediaFormat = format
                        mediaExtractor?.selectTrack(i)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaCodec?.configure(mediaFormat, surface, null, 0)
        mediaCodec?.start()
    }
}