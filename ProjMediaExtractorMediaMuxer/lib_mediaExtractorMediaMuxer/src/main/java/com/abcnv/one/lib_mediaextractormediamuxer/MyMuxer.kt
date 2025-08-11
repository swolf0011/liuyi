package com.abcnv.one.lib_mediaextractormediamuxer

import android.media.MediaCodec
import android.media.MediaFormat
import android.media.MediaMuxer
import java.io.File
import java.nio.ByteBuffer

class MyMuxer(path: String, var savePath: String, var listener: MyMuxerListener) {
    interface MyMuxerListener {
        fun onStart()
        fun onSuccess(path: String)
        fun onFail(msg: String)
    }

    val videoExtractor = MyExtractor(path)
    val audioExtractor = MyExtractor(path)

    lateinit var mediaMuxer: MediaMuxer

    var videoTrackId = -1
    var audioTrackId = -1
    var videoFormat: MediaFormat? = null
    var audioFormat: MediaFormat? = null

    var curSampleTime = -1L
    var curSampleFlags = -1

    init {
        initMediaMuxer()
    }

    fun initMediaMuxer() {
        try {
            videoFormat = videoExtractor.videoFormat
            audioFormat = audioExtractor.audioFormat

            val file = File(savePath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            mediaMuxer = MediaMuxer(file.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun start() {
        Thread({
            startHandler()
        }).start()
    }

    fun startHandler() {
        try {
            audioTrackId = -1
            videoTrackId = -1
            audioFormat?.let { audioTrackId = mediaMuxer.addTrack(it) }
            videoFormat?.let { videoTrackId = mediaMuxer.addTrack(it) }

            if (audioTrackId == -1 && videoTrackId == -1) {
                listener.onFail("没有任音频或视频数据")
                return
            }
            listener.onStart()
            mediaMuxer.start()

            val buffer = ByteBuffer.allocate(1024 * 1024)
            val info = MediaCodec.BufferInfo()

            var videoSize = 0

            do {
                videoSize = videoExtractor.readBuffer(buffer, false)
                if (videoSize > 0) {
                    info.offset = 0
                    info.size = videoSize
                    info.presentationTimeUs = videoExtractor.curSampleTime
                    info.flags = videoExtractor.curSampleFlags
                    mediaMuxer.writeSampleData(videoTrackId, buffer, info)
                }
            } while (videoSize > 0)

            var audioSize = 0
            do {
                audioSize = audioExtractor.readBuffer(buffer, false)
                if (audioSize > 0) {
                    info.offset = 0
                    info.size = videoSize
                    info.presentationTimeUs = audioExtractor.curSampleTime
                    info.flags = audioExtractor.curSampleFlags
                    mediaMuxer.writeSampleData(audioTrackId, buffer, info)
                }
            } while (audioSize > 0)
            //释放资源
            audioExtractor.release()
            videoExtractor.release()
            mediaMuxer.stop()
            mediaMuxer.release()
            listener.onSuccess(savePath)
        } catch (e: Exception) {
            e.printStackTrace()
            listener.onFail(e.message ?: "")
        }
    }
}