package com.abcnv.nvone.lib_https.test.data

/**
 * 网络请求的封面与纸张主题数据。
 *
 * @property category Int
 * @property categoryText String
 * @property isCover Int
 * @property coverPapers MutableList<CoverPaper>
 * @constructor
 */
data class CoverPaperCategory(
    var category:Int = 0,//主题 0色彩 1自然 2艺术 3用户自定义封面，5用户自定义纸张...，
    var categoryText:String = "",//主题title
    var isCover: Int,//CATEGORY_SHOW_COVER  0封面 CATEGORY_SHOW_Paper  1纸张 根据这个字段做排序与区分是封面的主题还是纸张的主题。
    var colourStatus: Int = 1//0 没有颜色(不显示颜色选择) 1 有颜色(显示颜色选择)
    ){
    var coverPapers = mutableListOf<CoverPaper>()//封面与纸张数据


    companion object{
        //下面主题是和服务后台统一定下的，不能乱改
        val CATEGORY_TYPE_OPENING_SEASON_PAPER = 1006//1006 开学季
        val CATEGORY_TYPE_BLANK_PAPER = 1000//1000 通用纸张
        val CATEGORY_TYPE_NATURAL_COVER = 1//1natural封面自然的
        val CATEGORY_TYPE_CUSTOM_COVER = 3//3用户自定义封面
        val CATEGORY_TYPE_CUSTOM_PAPER = 3000//3000用户自定义纸张
        //主题下的数据是封面或是纸张
        val CATEGORY_SHOW_COVER = 0//0封面
        val CATEGORY_SHOW_PAPER = 1//1纸张
    }
}
