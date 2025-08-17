package com.abcly.swolf.nymediacodecapp

import android.annotation.SuppressLint
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import com.abcly.swolf.mediacodecapp.NYPermissionUtil
import java.io.File
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {


    val root = Environment.getExternalStorageDirectory()
    val videoFile = File(root, "test.mp4")
    val inputAudio = "audio1.aac"
    val outputVideo = "video1.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onResume() {
        super.onResume()

        if (NYPermissionUtil.checkExternalStoragePermission(this)) {
            //提取视频分离出纯音频和纯视频文件
            extractorAndMuxerMP4(videoFile.absolutePath)
            //重新合成成音视频文件
            muxerMp4(inputAudio, outputVideo)
        } else {
            NYPermissionUtil.requestExternalStoragePermissions(this, 1000)
        }
    }

    //把音轨和视频轨再合成新的视频
    @SuppressLint("WrongConstant")
    private fun muxerMp4(inputAudio: String, outPutVideo: String): String {

        val videoFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "video.mp4")
        val audioFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), inputAudio)
        val outputFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), outPutVideo)


        if (outputFile.exists()) {
            outputFile.delete()
        }
        if (!videoFile.exists()) {
            return ""
        }
        if (!audioFile.exists()) {
            return ""
        }

        val videoExtractor = MediaExtractor()
        val audioExtractor = MediaExtractor()


        try {

            val mediaMuxer =
                MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            var videoTrackIndex = 0
            var audioTrackIndex = 0

            //先添加视频轨道
            videoExtractor.setDataSource(videoFile.absolutePath)
            val trackCountVideo = videoExtractor.trackCount

            for (i in 0 until trackCountVideo) {
                val trackFormat = videoExtractor.getTrackFormat(i)
                val mimeType = trackFormat.getString(MediaFormat.KEY_MIME)
                if (TextUtils.isEmpty(mimeType)) {
                    continue
                }
                if (mimeType!!.startsWith("video/")) {
                    videoExtractor.selectTrack(i)
                    videoTrackIndex = mediaMuxer.addTrack(trackFormat)
                    break
                }
            }
            //再添加音频轨道
            audioExtractor.setDataSource(audioFile.absolutePath)
            val trackCountAduio = audioExtractor.trackCount
            for (i in 0 until trackCountAduio) {
                val trackFormat = audioExtractor.getTrackFormat(i)
                val mimeType = trackFormat.getString(MediaFormat.KEY_MIME)
                if (TextUtils.isEmpty(mimeType)) {
                    continue
                }
                if (mimeType!!.startsWith("audio/")) {
                    audioExtractor.selectTrack(i)
                    audioTrackIndex = mediaMuxer.addTrack(trackFormat)
                    break
                }
            }

            //再进行合成
            mediaMuxer.start()
            val byteBuf = ByteBuffer.allocate(500 * 1024)

            val videoBufInfo = MediaCodec.BufferInfo()
            var videoSampleSize = 0



            do {
                videoSampleSize = videoExtractor.readSampleData(byteBuf, 0)
                if(videoSampleSize > 0){
                    videoBufInfo.flags = videoExtractor.sampleFlags
                    videoBufInfo.offset = 0
                    videoBufInfo.size = videoSampleSize
                    videoBufInfo.presentationTimeUs = videoExtractor.sampleTime
                    mediaMuxer.writeSampleData(videoTrackIndex, byteBuf, videoBufInfo)
                    videoExtractor.advance()
                }
            } while (videoSampleSize > 0)

            val audioBufInfo = MediaCodec.BufferInfo()
            var audioSampleSize = 0
            do {
                audioSampleSize = audioExtractor.readSampleData(byteBuf, 0)
                if(audioSampleSize > 0){
                    audioBufInfo.flags = audioExtractor.sampleFlags
                    audioBufInfo.offset = 0
                    audioBufInfo.size = audioSampleSize
                    audioBufInfo.presentationTimeUs = audioExtractor.sampleTime
                    mediaMuxer.writeSampleData(audioTrackIndex, byteBuf, audioBufInfo)
                    audioExtractor.advance()
                }
            } while (audioSampleSize > 0)

            //最后释放资源
            videoExtractor.release()
            audioExtractor.release()
            mediaMuxer.stop()
            mediaMuxer.release()

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
        return outputFile.absolutePath


    }

    private fun extractorAndMuxerMP4(url: String) {
        //提取数据（解封装）
        val mediaExtractor = MediaExtractor()
        try {
            //2.设置数据源，数据源可以是本地文件地址，也可以是网络地址：
            mediaExtractor.setDataSource(url)
            //3.获取轨道数
            val trackCount = mediaExtractor.trackCount
            //遍历轨道，查看音频轨或者视频轨道信息
            for (i in 0 until trackCount) {
                //4. 获取某一轨道的媒体格式
                val trackFormat = mediaExtractor.getTrackFormat(i)
                val keyMime = trackFormat.getString(MediaFormat.KEY_MIME)
                if (TextUtils.isEmpty(keyMime)) {
                    continue
                }
                //5.通过mime信息识别音轨或视频轨道，打印相关信息
                //(默认的是先扫描到视频，在扫描到音频)
                if (keyMime!!.startsWith("video/")) {
                    val outputFile = extractorAndMuxer(mediaExtractor, i, "/video.mp4")

                } else if (keyMime!!.startsWith("audio/")) {
                    val outputFile = extractorAndMuxer(mediaExtractor, i, "/audio.acc")
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("WrongConstant")
    private fun extractorAndMuxer(
        mediaExtractor: MediaExtractor,
        i: Int,
        outputName: String
    ): File {
        //获取传过来的MediaExtractor对应轨道的trackFormat
        val trackFormat = mediaExtractor.getTrackFormat(i)
        //选择轨道
        mediaExtractor.selectTrack(i)
        val outputFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), outputName)
        if (outputFile.exists()) {
            outputFile.delete()
        }
        //1. 构造MediaMuxer
        val mediaMuxer =
            MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        //2. 添加轨道信息 参数为MediaFormat
        mediaMuxer.addTrack(trackFormat)
        //3. 开始合成
        mediaMuxer.start()
        //4. 设置buffer
        val buf = ByteBuffer.allocate(500 * 1024)//设置每一帧的大小
        val bufInfo = MediaCodec.BufferInfo()
        //5.通过mediaExtractor.readSampleData读取数据流
        var sampleSize = 0
        //循环读取每帧的样本数据
        //mediaExtractor.readSampleData(buffer, 0)把指定通道中的数据按偏移量读取到ByteBuffer中
        do {
            sampleSize = mediaExtractor.readSampleData(buf, 0)
            if(sampleSize > 0){
                bufInfo.flags = mediaExtractor.sampleFlags
                bufInfo.offset = 0
                bufInfo.size = sampleSize
                bufInfo.presentationTimeUs = mediaExtractor.sampleTime
                //所有解码的帧都已渲染，我们现在可以停止播放了,虽然这里没有用到
                //一般的使用方法是判断 isEOS是否等于0；
                //int isEOS = bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                //判断输出数据是否为关键帧的方法：
                //boolean keyFrame = (bufferInfo.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
                //6. 把通过mediaExtractor解封装的数据通过writeSampleData写入到对应的轨道
                mediaMuxer.writeSampleData(0, buf, bufInfo)
                //读取下一帧数据
                mediaExtractor.advance()
            }
        } while (sampleSize > 0)

        mediaExtractor.unselectTrack(i)
        //6.关闭
        mediaMuxer.stop();
        mediaMuxer.release();
        return outputFile;
    }
}