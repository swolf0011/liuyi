package com.abcnv.one.lib_pen.data


data class NvStroke(
    val penType: NvPenType,
    val strokeWidth: Float = 1f,//笔宽
    val c0: String = "#FFFFFF",// 主颜色
    val c1: String = "#FFFFFF",// 边颜色
    val strokePointList: List<NvStrokePoint> = mutableListOf<NvStrokePoint>(),//一笔划的点集
)
