package com.abcnv.nvone.lib_https.test.data

/**
 *
 * 封面与纸张资源
 * 使用方式:
 *
 * 特殊说明:
 * Create by liuyi on 2022-06-13 15:54
 */
data class CoverPaperResources(

    var resFileUrl:String = "", //封面；这个也是pdf模板的文件url  没有时设置：/Cover/natural1
    var resThumbnailUrl:String = "" //这个也是 pdfTemplate缩略图url，线条pdfTemplate要透明背景图
    ){

    var resLocalFilePath:String = ""
    var resLocalThumbnailPath:String = ""

}
