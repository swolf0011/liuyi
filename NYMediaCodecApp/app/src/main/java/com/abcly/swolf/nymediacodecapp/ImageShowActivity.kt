package com.abcly.swolf.nymediacodecapp

import android.graphics.*
import android.media.Image
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class ImageShowActivity : AppCompatActivity() {

    val mVideoFile = File(Environment.getExternalStorageDirectory(),"text.mp4")
    val mVideoPath = mVideoFile.absolutePath

    lateinit var extractor:MediaExtractor//用于解封装
    var videoFormat:MediaFormat?=null//保存视频轨道的媒体格式
    lateinit var mediaCodec: MediaCodec//解码视频轨道资源


    var imageNum = 0
    var rotation = 0// rotation判断视频旋转角度，后面生成bitmap需要用到。
    var duration = 0L// duration /1000/1000可以判断我们需要取几帧，1s一帧，
    val YUV420P = 0
    val YUV420SP = 1
    val NV21 = 2

    lateinit var bytes:ByteArray
    var width:Int = 0
    var height:Int = 0
    //1.创建MediaExtractor和MediaCodec :  MediaExtractor负责解封装，MediaCodec负责解码视频轨道资源
    //2.解码获取图片，并进行转换：YUV_420_888-->NV21
    //3.YuvImage 加载nv21,转化成Bitmap用于显示。

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)

        getVideo()
    }

    /**
     *获取到视频轨道资源
     */
    private fun getVideo() {
        extractor = MediaExtractor()
        try {
            extractor.setDataSource(mVideoPath)
            val trackCount = extractor.trackCount
            for(i in 0 until trackCount){
                val trackFormat = extractor.getTrackFormat(i)
                val str = trackFormat.getString(MediaFormat.KEY_MIME)
                if(str != null && str!!.contains("video")){
                    videoFormat = trackFormat
                    extractor.selectTrack(i)
                    break
                }
            }

            if(videoFormat == null){
                return
            }
            videoFormat?.let {
                // 其中MediaFormat.KEY_COLOR_FORMAT 需要设置成COLOR_FormatYUV420Flexible
                // MediaCodec的所有硬件解码都支持这种格式
                it.setInteger(MediaFormat.KEY_COLOR_FORMAT,it.getInteger(MediaFormat.KEY_COLOR_FORMAT))
                it.setInteger(MediaFormat.KEY_WIDTH,it.getInteger(MediaFormat.KEY_WIDTH))
                it.setInteger(MediaFormat.KEY_HEIGHT,it.getInteger(MediaFormat.KEY_HEIGHT))
                if(it.containsKey(MediaFormat.KEY_ROTATION)){
                    //获取旋转多少的值
                    rotation = it.getInteger(MediaFormat.KEY_ROTATION)
                }
                //内容持续时间
                duration = it.getLong(MediaFormat.KEY_DURATION)
                //MediaCodec创建：createDecoderByType/createEncoderByType：根据特定MIME类型(如"video/avc")创建codec。
                //createByCodecName：知道组件的确切名称(如OMX.google.mp3.decoder)的时候，根据组件名创建codec。使用MediaCodecList可以获取组件的名称。
                val str = it.getString(MediaFormat.KEY_MIME)
                str?.let{mime->
                    mediaCodec = MediaCodec.createDecoderByType(mime)
                }

                //configure：配置解码器或者编码器。
                // 参数1：MediaFormat ，参数2：Surface ，参数3：MediaCrypto ，参数4：flags
                mediaCodec.configure(it,null,null,0)
                mediaCodec.start();
                //开始解码
                processByExtractor();

            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    //开始解码，获取帧序列
    private fun processByExtractor() {

        val bufferInfo = MediaCodec.BufferInfo()
        val timeOut = 5* 1000L
        var inputDone = false
        var outputDone = false
        var inputBuffers = mediaCodec.getInputBuffers()//TODO 0011

        var count = 0
        while(!outputDone){
            if(!inputDone){
//喂数据
                //如果是要获取所有帧序列，则不需要使用seekTo方法。
                //extractor.seekTo(count * intervalMs * 1000,MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                val inputBufferIndex = mediaCodec.dequeueInputBuffer(timeOut)
                if(inputBufferIndex >= 0){
                    val inputBuffer = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        mediaCodec.getInputBuffer(inputBufferIndex)
                    }else {
                        inputBuffers[inputBufferIndex]
                    }
                    val sampleDate = extractor.readSampleData(inputBuffer!!,0)

                    if(sampleDate > 0 && count * 1000 <= duration){
                        val sampleTime = extractor.getSampleTime()
                        val sampleFlags = extractor.getSampleFlags()

                        mediaCodec.queueInputBuffer(inputBufferIndex,0,sampleDate,sampleTime,0)
                        extractor.advance()
                        count++
                    }else {
                        //小于0，就说明读完了
                        mediaCodec.queueInputBuffer(inputBufferIndex,0,0,0,MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        inputDone = true
                    }
                }
            }
            if(!outputDone){
//获取数据
                val status = mediaCodec.dequeueOutputBuffer(bufferInfo,timeOut);
                if(status == MediaCodec.INFO_TRY_AGAIN_LATER){
                }else if(status == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
                }else if(status == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED){
                }else {
                    if((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0){
                        outputDone = true
                    }

                    val doRender = (bufferInfo.size !=0)
                    //获取图片并保存,getOutputImage格式是YUV_420_888
                    val image = mediaCodec.getOutputImage(status)
                    if(image == null){

                    }
                    mediaCodec.getOutputBuffer(status)
                    imageNum++
                    //dateFromImage(image);
                    //使用新方法来获取yuv数据
                    bytes = getBytesFromImageAsType(image!!,2)

                    //根据yuv数据获取Bitmap
                    val bitmap = getBitmapFromYUV(bytes,width,height,rotation)
                    //保存图片
                    if(bitmap != null){
                        //显示图片
                        val file = File(Environment.getExternalStorageDirectory(),"logo${imageNum}.png")
                        try {
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(file))
                        } catch (e:Exception) {
                            e.printStackTrace()
                        }
                        System.out.println("图片导入成功")
                    }

                    mediaCodec.releaseOutputBuffer(status,doRender)
                    //这里先尝试获取第一张图片
                    //break;

                }
            }
        }


    }



    private fun getBytesFromImageAsType(image: Image, type: Int): ByteArray {
        //获取源数据，如果是YUV格式的数据planes.length = 3
        //plane[i]里面的实际数据可能存在byte[].length <= capacity (缓冲区总大小)
        val planes = image.getPlanes()
        //数据有效宽度，一般的，图片width <= rowStride，这也是导致byte[].length <= capacity的原因
        // 所以我们只取width部分
        width = image.getWidth()
        height = image.getHeight()
        //此处用来装填最终的YUV数据，需要1.5倍的图片大小，因为Y U V 比例为 4:1:1 （这里是YUV_420_888）
        val yuvBytes = ByteArray(width * height * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8)
        //目标数组的装填到的位置
        var dstIndex = 0
        //临时存储uv数据的
        val uBytes = ByteArray(width * height / 4)
        val vBytes = ByteArray(width * height / 4)
        var uIndex = 0
        var vIndex = 0
        var pixelsStride = 0
        var rowStride = 0

        try {
            for (i in 0 until planes.size) {
                pixelsStride = planes[i].getPixelStride()
                rowStride = planes[i].getRowStride()

                val buffer = planes[i].buffer

                //如果pixelsStride==2，一般的Y的buffer长度=640*480，UV的长度=640*480/2-1
                //源数据的索引，y的数据是byte中连续的，u的数据是v向左移以为生成的，两者都是偶数位为有效数据
                val bytes = ByteArray(buffer.capacity())
                buffer.get(bytes)

                var srcIndex = 0
                if (i == 0) {
                    //直接取出来所有Y的有效区域，也可以存储成一个临时的bytes，到下一步再copy
                    for (j in 0 until height){
                        System.arraycopy(bytes, srcIndex, yuvBytes, dstIndex, width)
                        srcIndex += rowStride
                        dstIndex += width
                    }
                } else if (i == 1) {
                    //根据pixelsStride取相应的数据
                    for (j in 0 until height/2){
                        for (k in 0 until width/2){
                            uBytes[uIndex++] = bytes[srcIndex]
                            srcIndex += pixelsStride
                        }
                        if (pixelsStride == 2) {
                            srcIndex += rowStride - width;
                        } else if (pixelsStride == 1) {
                            srcIndex += rowStride - width / 2;
                        }
                    }
                } else if (i == 2) {
                    //根据pixelsStride取相应的数据
                    for (j in 0 until height/2){
                        for (k in 0 until width/2){
                            vBytes[vIndex++] = bytes[srcIndex];
                            srcIndex += pixelsStride;
                        }
                        if (pixelsStride == 2) {
                            srcIndex += rowStride - width;
                        } else if (pixelsStride == 1) {
                            srcIndex += rowStride - width / 2;
                        }
                    }
                }
            }
            //   image.close();
            //根据要求的结果类型进行填充
            when(type){
                YUV420P->{
                    System.arraycopy(uBytes, 0, yuvBytes, dstIndex, uBytes.size)
                    System.arraycopy(vBytes, 0, yuvBytes, dstIndex + uBytes.size, vBytes.size)
                }
                YUV420SP->{
                    for (i in 0 until vBytes.size){
                        yuvBytes[dstIndex++] = uBytes[i]
                        yuvBytes[dstIndex++] = vBytes[i]
                    }
                }
                NV21->{
                    for (i in 0 until vBytes.size){
                        yuvBytes[dstIndex++] = vBytes[i]
                        yuvBytes[dstIndex++] = uBytes[i]
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return yuvBytes
    }
    private fun getBitmapFromYUV(date: ByteArray, width: Int, height: Int, rotation: Int): Bitmap  {
        //使用YuvImage---》NV21
        val yuvImage = YuvImage(date, ImageFormat.NV21,width,height,null)
        val baos = ByteArrayOutputStream()

        yuvImage.compressToJpeg( Rect(0,0,width,height),20,baos)
        val jdate =baos.toByteArray()
        val bitmapFatoryOptions =  BitmapFactory.Options();
        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapFatoryOptions.inSampleSize = 4;
        if(rotation == 0){
            val bmp = BitmapFactory.decodeByteArray(jdate,0,jdate.size,bitmapFatoryOptions);
            return bmp;
        }else {
            val m = Matrix()
            m.postRotate(rotation+0.0f)
            val bmp = BitmapFactory.decodeByteArray(jdate,0,jdate.size,bitmapFatoryOptions)
            val bml = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),m,true)
            return bml;
        }
    }
}