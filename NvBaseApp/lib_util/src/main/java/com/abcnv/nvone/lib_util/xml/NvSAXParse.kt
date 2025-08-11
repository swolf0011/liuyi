package com.abcnv.nvone.lib_util.xml

import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.SAXParserFactory

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
object NvSAXParse {

    fun parse(handler: DefaultHandler, handlerValue: String) {
        try {
            val saxParserFactory = SAXParserFactory.newInstance()
            val saxParser = saxParserFactory.newSAXParser()
            saxParser.parse(InputSource(StringReader(handlerValue)), handler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parse(handler: DefaultHandler, inputStream: InputStream) {
        try {
            val saxParserFactory = SAXParserFactory.newInstance()
            val saxParser = saxParserFactory.newSAXParser()
            saxParser.parse(inputStream, handler)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun parse(handler: DefaultHandler, file: File) {
        try {
            val saxParserFactory = SAXParserFactory.newInstance()
            val saxParser = saxParserFactory.newSAXParser()
            saxParser.parse(file, handler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}