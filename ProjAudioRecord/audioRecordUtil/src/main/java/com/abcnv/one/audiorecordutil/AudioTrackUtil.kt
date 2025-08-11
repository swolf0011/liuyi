package com.abcnv.one.audiorecordutil

import android.content.Context
import android.content.Intent
import android.media.*
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

object AudioTrackUtil {


    fun playWAV(context: Context, pathWAV: String) {
        val file = File(pathWAV)
        if (file.exists()) {


            val uri = FileProvider.getUriForFile(context, "com.abcnv.one.projaudiorecord.fileprovider", file)
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "audio")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        }
    }

    fun playPCM_static(
        pathPCM: String,
        sampleRate: Int = 44100,
        setEncoding: Int = AudioFormat.ENCODING_PCM_16BIT,
        channel: Int = AudioFormat.CHANNEL_IN_STEREO,
    ):AudioTrack? {
        var baos:ByteArrayOutputStream? = null
        var fis: FileInputStream? = null
        var audioTrack: AudioTrack? = null
        try {
            val file = File(pathPCM)
            fis = FileInputStream(file)
            var len = 0
            val bs = ByteArray(1024 * 1024)
            baos = ByteArrayOutputStream()
            do {
                len = fis.read(bs)
                if (len > 0) {
                    baos.write(bs, 0, len)
                }
            } while (len > 0)

            val bytes = baos.toByteArray()
            try {
                baos.close()
                baos = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                fis.close()
                fis = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            val format = AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(setEncoding)
                .setChannelMask(channel)
                .build()

            audioTrack = AudioTrack(
                attributes,
                format,
                bytes.size,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )

            audioTrack.write(bytes, 0, bytes.size)
            audioTrack.play()
        } catch (e: Exception) {
            e.printStackTrace()

            try {
                baos?.close()
                baos = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                fis?.close()
                fis = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            audioTrack?.release()
            audioTrack = null

        }
        return audioTrack
    }

    fun stopPCM_static(audioTrack: AudioTrack?){
        audioTrack?.stop()
        audioTrack?.release()
    }

    private var isDone = false

    fun playPCM_stream(
        pathPCM: String,
        sampleRate: Int = 44100,
        setEncoding: Int = AudioFormat.ENCODING_PCM_16BIT,
        channel: Int = AudioFormat.CHANNEL_IN_STEREO,
    ) {
        isDone = false
        Thread(Runnable {
            var fis: FileInputStream? = null
            var audioTrack: AudioTrack? = null
            try {
                val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()

                val format = AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(setEncoding)
                    .setChannelMask(channel)
                    .build()

                val getMinBufferSize = AudioRecord.getMinBufferSize(
                    sampleRate,
                    channel,
                    setEncoding
                )

                audioTrack = AudioTrack(
                    attributes,
                    format,
                    getMinBufferSize,
                    AudioTrack.MODE_STREAM,
                    AudioManager.AUDIO_SESSION_ID_GENERATE
                )

                audioTrack.play()
                val file = File(pathPCM)
                fis = FileInputStream(file)
                var len = 0
                val bs = ByteArray(1024 * 1024)
                do {
                    len = fis.read(bs)
                    if (len > 0) {
                        audioTrack.write(bs, 0, len)
                    }
                } while (len > 0 && !isDone)

                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fis?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                audioTrack?.release()
            }
        }).start()
    }

    fun stopPCM_stream() {
        isDone = true
    }

}