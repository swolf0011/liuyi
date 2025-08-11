package com.abcnv.one.lib_mediaextractormediamuxer

import android.media.MediaExtractor
import android.media.MediaFormat
import java.nio.ByteBuffer

class MyExtractor(path: String) {
    lateinit var extractor: MediaExtractor
    var videoTrackId = -1
    var audioTrackId = -1
    var videoFormat: MediaFormat? = null
    var audioFormat: MediaFormat? = null

    val videoTrackIdList = mutableListOf<Int>()
    val videoFormatList = mutableListOf<MediaFormat>()
    val audioTrackList = mutableListOf<Int>()
    val audioFormatList = mutableListOf<MediaFormat>()

    var curSampleTime = -1L
    var curSampleFlags = -1

    init {
        initMedioExtactor(path)
    }

    fun initMedioExtactor(path: String) {
        try {
            extractor = MediaExtractor()
            extractor.setDataSource(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val numTracks = extractor.trackCount
        for (i in 0 until numTracks) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            mime?.let {
                if (it.startsWith("video") && videoFormat == null) {
                    // 视频轨
                    videoTrackId = i
                    videoFormat = format

                    videoTrackIdList.add(i)
                    videoFormatList.add(format)
                } else if (it.startsWith("audio") && audioFormat == null) {
                    // 音频轨
                    audioTrackId = i
                    audioFormat = format

                    audioTrackList.add(i)
                    audioFormatList.add(format)
                } else {

                }
            }
        }
    }

    fun readBuffer(buffer: ByteBuffer, isVideo: Boolean): Int {
        buffer.clear()

        val trackId = if (isVideo) {
            videoTrackId
        } else {
            audioTrackId
        }
        extractor.selectTrack(trackId)

        val count = extractor.readSampleData(buffer, 0)
        if (count < 0) {
            return -1
        }
        //记录当前时间戳
        curSampleTime = extractor.sampleTime
        //记录当前帧的标志位
        curSampleFlags = extractor.sampleFlags
        //进入下一帧
        extractor.advance()
        return count
    }

    fun release() {
        extractor.release()
    }
}