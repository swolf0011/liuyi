package com.abcly.swolf.nymediacodec.mediaCodecUtil

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ArrayBlockingQueue

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/5/8 18:06
 */
class NYH264Encoder(
    var m_width: Int,
    var m_height: Int,
    var m_framerate: Int,
    var bitRate: Int,
    path: String
) {
    private lateinit var mediaCodec: MediaCodec
    private lateinit var outputStream: BufferedOutputStream
    private lateinit var configbyte: ByteArray
    private val TIMEOUT_USEC = 12000
    private var isRuning = false

    companion object {
        private const val yuvqueuesize = 10
        var YUVQueue = ArrayBlockingQueue<ByteArray>(yuvqueuesize)
    }

    init {
        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, m_width, m_height)
        val formatYUV420Flexible = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, formatYUV420Flexible) //颜色格式
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, m_width * m_height * 5) //码率
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30) //帧率
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) //I 帧间隔

        try {
            // 创建 MediaCodec，此时是 Uninitialized 状态
//            val MIMETYPE_VIDEO_AVC = "video/avc"
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
            // 调用 configure 进入 Configured 状态
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            // 调用 start 进入 Executing 状态，开始编解码工作
            mediaCodec.start()

            createfile(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createfile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            outputStream = BufferedOutputStream(FileOutputStream(file))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun stopThread() {
        isRuning = false
        try {
            // 调用 stop 方法进入 Uninitialized 状态
            mediaCodec.stop()
            // 调用 release 方法释放，结束操作
            mediaCodec.release()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        try {
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //编码子线程
    fun startEncoderThread() {
        val EncoderThread = Thread {
            isRuning = true
            var input: ByteArray? = null
            var pts: Long = 0
            var generateIndex: Long = 0
            while (isRuning) {
                if (YUVQueue.size > 0) {
                    input = YUVQueue.poll()
                    val yuv420sp = ByteArray(m_width * m_height * 3 / 2)
                    NV21ToNV12(input, yuv420sp, m_width, m_height)
                    input = yuv420sp
                }
                if (input != null) {
                    try {
                        // 输入缓冲区
                        val inputBuffers = mediaCodec.inputBuffers
                        // 输出缓冲区
                        val outputBuffers = mediaCodec.outputBuffers
                        // 从输入缓冲区队列中取出可用缓冲区，并填充数据
                        val inputBufferIndex = mediaCodec.dequeueInputBuffer(-1)
                        if (inputBufferIndex >= 0) {
                            // 计算时间戳
                            pts = computePresentationTime(generateIndex)
                            val inputBuffer = inputBuffers[inputBufferIndex]
                            inputBuffer.clear()
                            inputBuffer.put(input)
                            mediaCodec.queueInputBuffer(inputBufferIndex, 0, input.size, pts, 0)
                            generateIndex += 1
                        }
                        val bufferInfo = MediaCodec.BufferInfo()
                        // 从输出缓冲区队列中拿到编解码后的内容，进行相应操作（这里是写入output h264文件）后释放，供下一次使用
                        var outputBufferIndex =
                            mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC.toLong())
                        while (outputBufferIndex >= 0) {
                            val outputBuffer = outputBuffers[outputBufferIndex]
                            val outData = ByteArray(bufferInfo.size)
                            outputBuffer[outData]
                            // flags 判断
                            when (bufferInfo.flags) {
                                2 -> {   // 配置相关的内容，也就是 SPS，PPS
                                    configbyte = ByteArray(bufferInfo.size)
                                    configbyte = outData
                                }
                                1 -> {   //关键帧
                                    val keyframe = ByteArray(bufferInfo.size + configbyte.size)
                                    System.arraycopy(
                                        configbyte,
                                        0,
                                        keyframe,
                                        0,
                                        configbyte.size
                                    )
                                    System.arraycopy(
                                        outData,
                                        0,
                                        keyframe,
                                        configbyte.size,
                                        outData.size
                                    )
                                    outputStream.write(keyframe, 0, keyframe.size)
                                }
                                else -> {      // 非关键帧和SPS、PPS,直接写入文件，可能是B帧或者P帧
                                    outputStream.write(outData, 0, outData.size)
                                }
                            }
                            mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                            outputBufferIndex =
                                mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC.toLong())
                        }
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                } else {
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        EncoderThread.start()
    }

    private fun NV21ToNV12(nv21: ByteArray?, nv12: ByteArray?, width: Int, height: Int) {
        if (nv21 == null || nv12 == null) {
            return
        }
        val framesize = width * height
        System.arraycopy(nv21, 0, nv12, 0, framesize)
        var i = 0
        while (i < framesize) {
            nv12[i] = nv21[i]
            i++
        }
        var j = 0
        while (j < framesize / 2) {
            nv12[framesize + j - 1] = nv21[j + framesize]
            j += 2
        }
        j = 0
        while (j < framesize / 2) {
            nv12[framesize + j] = nv21[j + framesize - 1]
            j += 2
        }
    }

    /**
     * Generates the presentation time for frame N, in microseconds.
     */
    private fun computePresentationTime(frameIndex: Long): Long {
        return 132 + frameIndex * 1000000 / m_framerate
    }

}