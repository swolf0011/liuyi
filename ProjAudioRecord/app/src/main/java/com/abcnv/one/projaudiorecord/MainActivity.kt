package com.abcnv.one.projaudiorecord

import android.app.Activity
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.abcnv.one.audiorecordutil.AudioRecordUtil
import com.abcnv.one.audiorecordutil.AudioTrackUtil
import com.abcnv.one.audiorecordutil.NvFileUtil
import com.abcnv.one.audiorecordutil.Util
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    lateinit var activityResultLauncher0: ActivityResultLauncher<Array<String>>
    lateinit var ps0: Array<String>

    var pathPCM = ""
    var pathWAV = ""

    var isRecording = false
    var isPlaying = false

    var audioTrack: AudioTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pathPCM = Util.getCreatePath(this, "test.pcm")
        pathWAV = Util.getCreatePath(this, "test.wav")
        Log.d("0011==","pathPCM::${pathPCM}")
        Log.d("0011==","pathWAV::${pathWAV}")

        findViewById<Button>(R.id.button).setOnClickListener {
            if (isRecording) {
                //录音中,去停止
                AudioRecordUtil.stopRecord()
                findViewById<Button>(R.id.button).text ="开始录音"
				isRecording = false
            } else {
                findViewById<Button>(R.id.button).text ="停止录音"
                //还没有录音,去录
                activityResultLauncher0.launch(ps0)
				isRecording = true
            }
        }
        findViewById<Button>(R.id.buttonWAV).setOnClickListener {
            AudioTrackUtil.playWAV(this, pathWAV)
            findViewById<Button>(R.id.buttonWAV).text ="停止播放WAV"
        }
        findViewById<Button>(R.id.buttonPCM_static).setOnClickListener {
            if (isPlaying) {
                findViewById<Button>(R.id.button).text ="开始播放PCM_static"
                AudioTrackUtil.stopPCM_static(audioTrack)
				isPlaying = false
            } else {
                findViewById<Button>(R.id.button).text ="停止播放PCM_static"
                audioTrack = AudioTrackUtil.playPCM_static(
                    pathPCM,
                    44100,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioFormat.CHANNEL_IN_STEREO
                )
				isPlaying = true
            }
        }
        findViewById<Button>(R.id.buttonPCM_stream).setOnClickListener {
            if (isPlaying) {
                findViewById<Button>(R.id.button).text ="开始播放PCM_stream"
                AudioTrackUtil.stopPCM_stream()
				isPlaying = false
            } else {
                findViewById<Button>(R.id.button).text ="停止播放PCM_stream"
                AudioTrackUtil.playPCM_stream(
                    pathPCM,
                    44100,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioFormat.CHANNEL_IN_STEREO
                )
				isPlaying = true
            }
        }
        checkPermission(this)


    }

    private fun checkPermission(actvity: AppCompatActivity) {
        val pair0 = Util.checkPermission(actvity) {
            startRecord()

        }
        activityResultLauncher0 = pair0.first
        ps0 = pair0.second
    }


    private fun startRecord() {
        val audioRecord = AudioRecordUtil.initAudioRecord(this)
        if (audioRecord != null) {
            isRecording = true
            AudioRecordUtil.startRecord(
                audioRecord,
                pathPCM,
                pathWAV,
                44100,
                AudioFormat.CHANNEL_IN_STEREO
            )
        } else {
            Toast.makeText(this, "AudioRecord initial fail!", Toast.LENGTH_SHORT).show()
        }
    }


}