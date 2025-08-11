package com.abcnv.nvone.lib_util.xml

import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.OutputStream

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi

 * @DATE 2023/4/20 14:21
 */
abstract class NvPullParse<T> {

    lateinit var factory: XmlPullParserFactory

    init {
        try {
            factory = XmlPullParserFactory.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun save(outputStream: OutputStream, list: List<T>)

    abstract fun parse(inputStream: InputStream): List<T>
}