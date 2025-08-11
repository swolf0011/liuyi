package com.abcnv.one.audiorecordutil

import android.Manifest
import android.R.attr.data
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


object AudioRecordUtil {


    var getMinBufferSize = 0
    fun initAudioRecord(
        context: Context,
        sampleRate: Int = 44100,
        setEncoding: Int = AudioFormat.ENCODING_PCM_16BIT,
        channel: Int = AudioFormat.CHANNEL_IN_STEREO
    ): AudioRecord? {
        getMinBufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            channel,
            setEncoding
        )
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        return AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channel,
            setEncoding,
            getMinBufferSize
        )
    }

    private var isDone = false

    fun startRecord(
        audioRecord: AudioRecord,
        pathPCM: String,
        pathWAV: String,
        sampleRateInHz: Int = 44100,
        channels: Int = AudioFormat.CHANNEL_IN_STEREO
    ) {
        isDone = false
        Thread(Runnable {
            var fosPCM: FileOutputStream? = null
            var fosWAV: FileOutputStream? = null
            var fisPCM: FileInputStream? = null
            try {
                val filePCM = File(pathPCM)
                if (!filePCM.parentFile.exists()) {
                    filePCM.parentFile.mkdirs()
                }
                fosPCM = FileOutputStream(filePCM)

                audioRecord.startRecording()
                val bs = ByteArray(1024 * 1024)
                while (!isDone) {
                    val len = audioRecord.read(bs, 0, bs.size)
                    if (AudioRecord.ERROR_INVALID_OPERATION != len) {
                        fosPCM.write(bs, 0, len)
//                        fosWAV?.write(bs, 0, len)
                    }
                }
                audioRecord.stop()
                audioRecord.release()
                fosPCM.flush()

                val fileWAV = File(pathWAV)
                if (!fileWAV.parentFile.exists()) {
                    fileWAV.parentFile.mkdirs()
                }
                fisPCM = FileInputStream(filePCM)
                fosWAV = FileOutputStream(fileWAV)

                val channels0 = if (channels === AudioFormat.CHANNEL_IN_MONO) 1 else 2
                val byteRate = 16 * sampleRateInHz * channels0 / 8
                val totalAudioLen = fisPCM.getChannel().size()
                val totalDataLen = totalAudioLen+36
                val header = generateWavFileHeader(
                    totalAudioLen,
                    totalDataLen,
                    sampleRateInHz,
                    channels0,
                    byteRate
                )
                fosWAV.write(header, 0, header.size)
                fosWAV.flush()
                val data = ByteArray(getMinBufferSize)
                while (fisPCM.read(data) != -1) {
                    fosWAV.write(data)
                    fosWAV.flush()
                }
                Log.d("0011==", "录间完成::${pathWAV}")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fosPCM?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    fisPCM?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    fosWAV?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                audioRecord.release()
            }
        }).start()
    }

    fun stopRecord() {
        isDone = true
    }


    fun generateWavFileHeader(
        totalAudioLen: Long,
        totalDataLen: Long,
        longSampleRate: Int,
        channels: Int,
        byteRate: Int
    ): ByteArray {
        val header = ByteArray(44)
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xffL).toByte()
        header[5] = (totalDataLen shr 8 and 0xffL).toByte()
        header[6] = (totalDataLen shr 16 and 0xffL).toByte()
        header[7] = (totalDataLen shr 24 and 0xffL).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (channels * 16 / 8).toByte()
        header[33] = 0
        header[34] = 16
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (totalAudioLen and 0xffL).toByte()
        header[41] = (totalAudioLen shr 8 and 0xffL).toByte()
        header[42] = (totalAudioLen shr 16 and 0xffL).toByte()
        header[43] = (totalAudioLen shr 24 and 0xffL).toByte()
        return header
    }

}