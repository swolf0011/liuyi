package com.abcnv.one.lib_pen.data

enum class NvGraphType(private val v: Int) {
    BallpointPen(0),//圆珠笔
    GraphPen(1),//图形
    HighlighterPen(2),//荧光笔
    Pen(3), //钢笔
    Pencil(4),//铅笔
    PicturePen(5),//图片
    TxtPen(6)//文本

    /**
     *
     *
     线
     矩形
     圆形
     椭圆形
     三角形
     正边形
     左箭头
     右箭头
     双箭头
     立方体
     锥体
     圆柱体
     球体




     */
}