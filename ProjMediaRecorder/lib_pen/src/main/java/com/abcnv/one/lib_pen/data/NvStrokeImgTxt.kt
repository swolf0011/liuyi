package com.abcnv.one.lib_pen.data



data class NvStrokeImgTxt(
    val penType: NvPenType,
    val x: Float,//x位置
    val y: Float,//y位置
    val w: Int = 1,//图片文本宽
    val h: Int = 1,//图片文本高

    val content: String = ""//图片path或设置样式的文本
) {

}
