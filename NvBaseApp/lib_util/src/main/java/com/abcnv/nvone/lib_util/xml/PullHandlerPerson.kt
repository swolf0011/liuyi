package com.abcnv.nvone.lib_util.xml

import org.xmlpull.v1.XmlPullParser
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

 * @DATE 2023/4/20 14:34
 */
class PullHandlerPerson : NvPullParse<Person>() {
    override fun save(outputStream: OutputStream, list: List<Person>) {
    }

    override fun parse(inputStream: InputStream): List<Person> {
        val list = mutableListOf<Person>()
        var person: Person? = null
        var elementName = ""
        try {
            val xpp = factory.newPullParser()
            xpp.setInput(inputStream, "UTF-8")
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {

                } else if (eventType == XmlPullParser.START_TAG) {
                    elementName = xpp.name // 取得元素名称
                    if ("person" == elementName) { //
                        // 如果是person节点
                        person = Person() // 实例化对象
                        val count = xpp.attributeCount
                        for (i in 0 until count) {
                            if (xpp.getAttributeName(i) == "id") {
                                person._id = xpp.getAttributeValue(i).toInt()
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && person != null) {
                    // 结束标记
                    if ("person" == xpp.name) {
                        list.add(person) // 保存对象
                        person = null // 清空对象
                    }
                    elementName = ""
                } else if (eventType == XmlPullParser.TEXT && person != null) {
                    // 元素内容
                    if ("name" == elementName) { // 如果是name节点
                        person.name = xpp.text // 取出name节点元素
                    } else if ("pwd" == elementName) { // 如果是pwd节点
                        person.pwd = xpp.text // 取出pwd节点元素
                    } else if ("age" == elementName) { // 如果是age节点
                        person.age = xpp.text // 取出age节点元素
                    }
                }
                eventType = xpp.next() // 取得下一个事件码

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        return list
    }
}