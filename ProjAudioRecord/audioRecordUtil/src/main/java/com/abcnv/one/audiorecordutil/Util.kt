package com.abcnv.one.audiorecordutil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun getCreatePath(activity: Activity, name: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val curDate = Date(System.currentTimeMillis())
        val curTime: String = formatter.format(curDate).replace(" ", "")
        val dir = NvFileUtil.getExternalFilesDir(activity)
        val path = "${dir.absolutePath}/file_${curTime}_${name}"
        return path
    }

    /**
     * 申请录音的权限
     */
    fun checkPermission(
        activity: AppCompatActivity,
        callback: () -> Unit
    ): Pair<ActivityResultLauncher<Array<String>>, Array<String>> {

        val write_external = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val read_external = Manifest.permission.READ_EXTERNAL_STORAGE
        val record_audio = Manifest.permission.RECORD_AUDIO
        val ps = arrayOf(write_external, read_external, record_audio)
        val requestMultiplePermissions = ActivityResultContracts.RequestMultiplePermissions()
        requestMultiplePermissions.createIntent(activity, ps)
        val activityResultLauncher =
            activity.registerForActivityResult(requestMultiplePermissions) {
                var result = true
                it.keys.forEach { key ->
                    if (it[key] == null || it[key] == false) {
                        result = false
                    }
                }
                if (result) {
                    callback.invoke()
                }
            }
        return Pair(activityResultLauncher, ps)
    }

    fun playWAV(context: Context, pathWAV: String) {
        val file = File(pathWAV)
        if (file.exists()) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri = FileProvider.getUriForFile(context, "my_image", file)
            intent.setDataAndType(uri, "audio")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        }
    }

    fun playPCM_static(pathPCM: String,
        sampleRate: Int = 44100,
        setEncoding: Int = AudioFormat.ENCODING_PCM_16BIT,
        channel: Int = AudioFormat.CHANNEL_IN_STEREO,
    ) {
        val baos = ByteArrayOutputStream()
        var fis: FileInputStream? = null
        var audioTrack:AudioTrack? = null
        try {
            val file = File(pathPCM)
            fis = FileInputStream(file)
            var len = 0
            val bs = ByteArray(1024 * 1024)
            do {
                len = fis.read(bs)
                if(len > 0){
                    baos.write(bs, 0, len)
                }
            } while (len > 0)

            val bytes = baos.toByteArray()

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
        } finally {
            try {
                baos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                fis?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            audioTrack?.release()
        }

    }

    fun playPCM_stream(pathPCM: String,
                       sampleRate: Int = 44100,
                       setEncoding: Int = AudioFormat.ENCODING_PCM_16BIT,
                       channel: Int = AudioFormat.CHANNEL_IN_STEREO,
    ) {
        var fis: FileInputStream? = null
        var audioTrack:AudioTrack? = null
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
                if(len > 0){
                    audioTrack.write(bs, 0, len)
                }
            } while (len > 0 )

            audioTrack.stop()
            audioTrack.release()

        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            try {
                fis?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            audioTrack?.release()
        }
    }

}