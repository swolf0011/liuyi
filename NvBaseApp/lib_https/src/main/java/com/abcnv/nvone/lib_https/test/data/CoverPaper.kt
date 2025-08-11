package com.abcnv.nvone.lib_https.test.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.Exception

/**
 *
 * 封面与纸张
 * 使用方式:
 *
 * 特殊说明:
 * Create by liuyi on 2022-06-13 15:52
 */
data class CoverPaper(var id:String = "") {

    var vip:Int = VIP_0//是否为VIP资源， 0不是 1是vip
    var name:String = ""//背景名字
    var paperName:String = ""//绘制纸张Name,为了找对应绘制纸张方法
    var fillStyle:Int = 0
    var direction:Int = DIRECTION_VERTICAL//横版竖版 2竖版,1横版,0正方形版
    var paperType:String ="A4"//纸张类型 A4\A0\信纸\法律纸\账簿\自定义...
    var coverPaperType:Int = 1
    var resources: CoverPaperResources = CoverPaperResources()//资源
    var paperWidth:Float = 0f
    var paperHeight:Float = 0f
    var colour:Int = -1 //-1无色，0白色，1米色，2深灰色，这介后台返回的数据，只为过虑颜色。

    var isCover: Int =
        CoverPaperCategory.CATEGORY_SHOW_COVER//CATEGORY_SHOW_COVER  0封面 CATEGORY_SHOW_Paper  1纸张，CoverPaperAdapter适配器使用


    var categoryNum:Int = 0 //主题
    var categoryText:String = ""

    var isSelect = false//是否选择
    var isDeleteSelect = false//长按删除的选择
    var isDistanceAdjustment:Boolean = false//是否距离调节

    var isSysDefData:Boolean = false//是否为默认

    var isMarketEntrance = false // 内容集市入口

    fun isEqualsOfId(cp: CoverPaper):Boolean{
        return id.equals(cp.id)
    }

    companion object{
        //会员 0不是 1是vip
        val VIP_0 = 0
        val VIP_1 = 1
        //1 下架,0 上架
        val STATUS_UPPER = 0
        val STATUS_LOWER = 1
//        //填充方式 0全局,1裁剪
//        val FILLSTYLE_ALL = 0
//        val FILLSTYLE_CROP = 1
        //横版竖版 2竖版,1横版,0正方形版
        val DIRECTION_SQUARE = 0
        val DIRECTION_HORIZONTAL = 1
        val DIRECTION_VERTICAL = 2
        val DIRECTION_PDF = 3
        val DIRECTION_ALL = 4
        //1封面，2纸张
        val SHOWTYPE_COVER = 1
        val SHOWTYPE_PAPER = 2

        //添加自定义封面第一个拍照相册item
        var ID_CUSTOM_10000 = "ID_CUSTOM_10000"
        //添加自定义封面第二个空背景item
        var ID_CUSTOM_10001 = "ID_CUSTOM_10001"
        //超9个自定义封面item
        var ID_CUSTOM_19999 = "ID_CUSTOM_19999"


        //添加自定义纸张第一个拍照相册item
        var ID_CUSTOM_20000 = "ID_CUSTOM_20000"
        //添加自定义纸张第二个空背景item
//        var ID_CUSTOM_20001 = "ID_CUSTOM_20001"

        // 内容集市入口
        var ID_MARKET_ENTRANCE = "ID_MARKET_ENTRANCE"

        fun transJsonToObj(json:String): CoverPaper?{
            try {
                val type = object : TypeToken<CoverPaper>() {}.type
                var cp = Gson().fromJson<CoverPaper>(json, type)
                return cp
            }catch (e:Exception){
                e.printStackTrace()
            }
            return null
        }

        fun transJsonToObjList(jsonList:String):ArrayList<CoverPaper>{
            try {
                val type = object : TypeToken<ArrayList<CoverPaper>>() {}.type
                var list = Gson().fromJson<ArrayList<CoverPaper>>(jsonList, type)
                return list
            }catch (e:Exception){
                e.printStackTrace()
            }
            return ArrayList<CoverPaper>()
        }

        fun toJsonStr(cp: CoverPaper):String{
            return Gson().toJson(cp)
        }
    }
    enum class CoverPaperType(var type:String, var w:Float, var h:Float){

        A4("A4",1240f,1754f),
//        val PAPERTYPE_A6 = "A6"
//        val PAPERTYPE_A5 = "A5"
//        val PAPERTYPE_A4 = "A4"
//        val PAPERTYPE_A3 = "A3"
//        val PAPERTYPE_A2 = "A2"
//        val PAPERTYPE_A1 = "A1"
//        val PAPERTYPE_LETTER_PAPER = "信纸"
//        val PAPERTYPE_LEGAL_PAPER = "法律纸"
//        val PAPERTYPE_ACCOUNT_BOOKS = "账簿"
//        val PAPERTYPE_CUSTOM = "自定义"
    }

    override fun toString(): String {
        return "categoryText:${categoryText},name:${name},categoryNum:${categoryNum},W:${paperWidth},H:${paperHeight},d:${direction}," +
                "\ncoverPaperType:${coverPaperType},bgTyped:," +
                "\nRES:${resources.resThumbnailUrl}"
    }
    fun p(): String {
        return "W:${paperWidth},,H:${paperHeight},,d:${direction},,name:${name},,id:${id},,"
    }
    fun clone(): CoverPaper {
        var cp = CoverPaper()
        cp.id = id
        cp.vip = vip
        cp.name = name
        cp.paperName = paperName
        cp.fillStyle = fillStyle
        cp.direction = direction
        cp.paperType = paperType
        cp.coverPaperType = coverPaperType

        if(resources != null){
            cp.resources.resFileUrl = resources.resFileUrl
            cp.resources.resThumbnailUrl = resources.resThumbnailUrl
            cp.resources.resLocalFilePath = resources.resLocalFilePath
            cp.resources.resLocalThumbnailPath = resources.resLocalThumbnailPath
        }

        cp.colour = colour
        cp.paperWidth = paperWidth
        cp.paperHeight = paperHeight
        cp.categoryNum = categoryNum
        cp.categoryText = categoryText
        cp.isSelect = isSelect
        cp.isDistanceAdjustment = isDistanceAdjustment




        return cp
    }
}
