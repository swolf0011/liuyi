package com.abcnv.nvone.lib_util.xml

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi

 * @DATE 2023/4/20 14:26
 */
class SAXHandlerPerson() : DefaultHandler(){
    val list = mutableListOf<Person>()

    var tagName = ""
    var person: Person? = null

    override fun startDocument() {
        super.startDocument()
    }
    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        super.startElement(uri, localName, qName, attributes)
        tagName = localName
        if ("person" == tagName) {
            person = Person()
            val length = attributes.length
            for (i in 0 until length) {
                if (attributes.getLocalName(i) === "id") {
                    person!!._id = attributes.getValue(i).toInt()
                }
                // String attrName = attributes.getLocalName(i);
                // String attrValue = attributes.getValue(i);
            }
        }
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        super.endElement(uri, localName, qName)
        if ("person" == localName && person != null) {
            list.add(person!!)
        }
        tagName = ""
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        super.characters(ch, start, length)
        val value = String(ch, start, length)
        person?.let {
            if ("name" == tagName) {
                it.name = value
            } else if ("pwd" == tagName) {
                it.pwd = value
            } else if ("age" == tagName) {
                it.age = value
            }
        }
    }


    override fun endDocument() {
        super.endDocument()
    }

}