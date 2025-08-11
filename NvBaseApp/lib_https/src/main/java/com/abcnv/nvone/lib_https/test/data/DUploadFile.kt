package com.abcnv.nvone.lib_https.test.data

data class DUploadFile(var fileId:String = "", var fileUrl:String = ""){

    override fun toString(): String {
        return "{${fileId},${fileUrl}}"
    }
}