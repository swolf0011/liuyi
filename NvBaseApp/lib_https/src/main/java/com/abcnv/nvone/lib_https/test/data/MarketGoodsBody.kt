package com.abcnv.nvone.lib_https.test.data

data class MarketGoodsBody(
    var categoryKey: String?,
    var pageNum: Int,
    var pageSize: Int = 10,
    // 上架时间 1：升序 2：降序
    var listingTime: Int = 0,
    // 价格排序 1：升序 2：降序
    var priceSort: Int = 0,
    // 销量排序1：升序 2：降序
    var salesSort: Int = 0,
    // 标签ID
    var tagKey: String? = null,
    // 用户是否免费标识 0：付费 1：免费
    var freeFlag: Int = 0,
    // 商品热卖标识 0：常规 1：热卖商品 2：爆款商品
    var hotFlag: Int = 0,
    // 商品价格标识 0：付费 1：免费 2：会员免费
    var priceFlag: Int = 0,
    // 创作者ID
    var creatorId: Int = 0,
)