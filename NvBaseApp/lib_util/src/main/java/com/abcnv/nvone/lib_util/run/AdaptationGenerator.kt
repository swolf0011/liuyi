package com.abcnv.nvone.lib_util.run

import java.io.*
import java.lang.String

object AdaptationGenerator {
    //最小大小，单位：px
    private const val MIN_SIZE = -100
    //最大大小，单位：px
    private const val MAX_SIZE = 1280
    //宽度基准，单位：px
    private const val WIDTH_BASE = 826
    //高度基准，单位：px
    private const val HEIGHT_BASE = 1200
    //屏幕最小宽度基准，单位：dp
//    private const val SW_BASE = 760f
//    private const val FILE_KEY = ""
//    private const val NAME_KEY = "dp"

    private const val SW_BASE = 826f
    private const val FILE_KEY = ""
    private const val NAME_KEY = "dp"



//    private val TABLET_SUPPORT_SW = floatArrayOf(
//        0f, 160f, 180f, 200f,
//        220f, 240f, 260f, 280f, 300f,
//        320f, 340f, 360f, 380f, 400f,
//        420f, 440f, 460f, 480f, 500f,
//        520f, 540f, 560f, 580f, 600f,
//        620f, 640f, 660f, 680f, 700f,
//        720f, 740f, 760f, 780f, 800f,
//        820f, 840f, 860f, 880f, 900f,
//        920f, 940f, 960f, 980f, 1000f,
//        1020f, 1040f, 1060f, 1080f, 1100f,
//        1120f, 1140f, 1160f, 1180f, 1200f,
//        1220f, 1240f, 1260f, 1280f, 1300f,
//        1320f, 1340f, 1360f, 1380f, 1400f,
//        1420f, 1440f, 1460f, 1480f, 1500f,
//        1520f, 1540f, 1560f, 1580f, 1600f,
//        1720f, 1740f, 1760f, 1780f, 1800f,
//        1820f, 1840f, 1860f, 1880f, 1900f,
//        1920f, 1940f, 1960f, 1980f, 2000f
//        )


    fun multiscreen(){
        /**
         * 目标屏幕最小宽度集，值可选如下
         * @see TABLET_SUPPORT_SW 平板所支持屏幕最小宽度集
         * @see PHONE_SUPPORT_SW 手机所支持屏幕最小宽度集
         */
        //目录生成路劲 注：如有路劲不一致，请手动修改
        val path = System.getProperty("user.dir") + "/multiscreen/src/main/res"
        val directory = File(path)
        if (directory.exists()) {
            directory.delete()
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //平板所支持屏幕最小宽度集，单位：dp注：0 代表默认支持尺寸(values 目录下)
        forEachIndexedMultiscreen(path,0,0f)
        //如：基数为760的密度为1，最小宽度为values-sw760dp。1600的密度为2.5，最小宽度为values-sw640dp
        //如：基数为640的密度为1，最小宽度为values-sw640dp。1600的密度为2.5，最小宽度为values-sw640dp
        for(i in 160 .. 2000 step 20){
            var ws = i+0.0f
            forEachIndexedMultiscreen(path,i,ws)
        }
    }

    fun forEachIndexedMultiscreen(path: kotlin.String, index:Int, fl:Float){
        var parent:File
        var width:Float
        if(0< fl){
            width = fl
            parent = File(path + File.separator + "values-sw${index}dp")
        }else{
            width = SW_BASE / 1f
            parent = File(path + File.separator + "values")
        }
        if (!parent.exists()) {
            parent.mkdirs()
        }
        val file = File(parent, "dimens$FILE_KEY.xml")
        if (file.exists() && file.isFile()) {
            file.delete()
        }
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val sb = StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sb.append("<resources>\n")
        for (i in MIN_SIZE..MAX_SIZE) {
            sb.append('\t')
            sb.append("<dimen name=\"${NAME_KEY}")
            if (i < 0) {
                sb.append("n")
            }
            sb.append(Math.abs(i))
            sb.append("\">")
            sb.append(String.format("%.1f", i * (width / SW_BASE)))
            sb.append("dp</dimen>\n")
        }

        sb.append("\t<dimen name=\"${NAME_KEY}05\">")
        sb.append(String.format("%.1f", 0.5f * (width / SW_BASE)))
        sb.append("dp</dimen>\n")
        sb.append("</resources>")
        try {
            val writer = PrintWriter(FileOutputStream(file))
            writer.print(sb.toString())
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}

fun main(){
    AdaptationGenerator.multiscreen()
    println("完成!!")
}
