package com.abcnv.nvone.lib_https.test.data

class MarketGoodsPage() {

    var list: ArrayList<MarketGoods> = ArrayList()

    //当前页
    var pageNum = 0

    //每页的数量
    var pageSize = 0

    //当前页的数量
    var size = 0

    //总页数
    var pages = 0

    //前一页
    var prePage = 0

    //下一页
    var nextPage = 0

    //是否为第一页
    var isFirstPage = false

    //是否为最后一页
    var isLastPage = false

    //是否有前一页
    var isHasPreviousPage = false

    //是否有下一页
    var isHasNextPage = false

    //由于startRow和endRow不常用，这里说个具体的用法
    //可以在页面中"显示startRow到endRow 共size条数据"
    //当前页面第一个元素在数据库中的行号
    var startRow = 0

    //当前页面最后一个元素在数据库中的行号
    var endRow = 0
    //导航页码数
    var navigatePages = 0

    //导航条上的第一页
    var navigateFirstPage = 0

    //导航条上的最后一页
    var navigateLastPage = 0


}