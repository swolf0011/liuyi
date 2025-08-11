package com.abcnv.nvone.lib_util.xml

import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.SAXParserFactory

object NvXmlUtil {
    fun paseStr(handler: DefaultHandler, str: String) {
        val saxParserFactory = SAXParserFactory.newInstance()
        val saxParser = saxParserFactory.newSAXParser()
        val sr = StringReader(str);
        val input = InputSource(sr);
        saxParser.parse(input,handler)
    }
    fun paseIO(handler: DefaultHandler,input: InputStream){
        val saxParserFactory = SAXParserFactory.newInstance()
        val saxParser = saxParserFactory.newSAXParser()
        saxParser.parse(input,handler)
    }

    fun paseFile(handler: DefaultHandler,file: File){
        val saxParserFactory = SAXParserFactory.newInstance()
        val saxParser = saxParserFactory.newSAXParser()
        saxParser.parse(file,handler)
    }
}