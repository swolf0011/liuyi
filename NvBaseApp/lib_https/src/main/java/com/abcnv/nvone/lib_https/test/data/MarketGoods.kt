package com.abcnv.nvone.lib_https.test.data

import android.os.Parcel
import android.os.Parcelable

/**
 * @author: haosl
 * on: 2023/12/4
 *
 * categoryId	integer($int32)
 * 类目ID
 * categoryName	string
 * 类目名称
 * description	string
 * 描述
 * goodsName	string
 * 商品名称
 * goodsNo	string
 * 商品编号
 * id	integer($int32)
 * 主键ID
 * originalPrice	number
 * 原始价格
 * pictureUrl	string
 * 图片url，多个逗号分隔
 * tagName	string
 * 标签名称
 * thumbnailUrl	string
 * 缩略图url
 */
class MarketGoods() : Parcelable {

    var categoryKey: String? = ""
    var description: String? = ""
    var goodsId: Int = 0
    var goodsName: String? = ""
    var goodsNo: String? = ""
    var goodsResource: GoodsResource? = null
    //商品原始价格，如果有折扣显示折后的价格（兼容老版本），二期开始，此字段废弃
    var originalPrice: String? = ""
    var pictureUrls: String? = ""

    /**
     * 0  显示价格
     * 1  显示   创建
     * 2  显示  "已创建"
     */
    var purchaseStatus: Int = 0 //
    var status: Int = 0
    var tagName: String? = ""
    var thumbnailUrl: String? = ""
    /**是否免费 0：付费 1：免费*/
    var freeFlag: Int = 0
    // 折扣
    var discount: Float = 0f
    // 商品折扣后的价格
    var discountPrice: String? = ""
    // 商品热卖标识 0：常规 1：热卖商品 2：爆款商品
    var hotFlag: Int = 0

    var tagKey: String? = ""
    // 商品真实价格
    var realPrice: String? = ""
    // "商品的最终售价
    var finalPrice: String? = ""

    // 商品优惠标识 0：无优惠 1：折扣优惠
    var preferentialFlag: Int = 0

    // 商品价格标识 0：付费 1：免费 2：会员免费
    var priceFlag: Int = 0
    // 版权标识 0：无限制 1：保密级别高，不可导出、备份、上传
    var copyrightFlag: Int = 0

    constructor(parcel: Parcel) : this() {
        categoryKey = parcel.readString()
        description = parcel.readString()
        goodsId = parcel.readInt()
        goodsName = parcel.readString()
        goodsNo = parcel.readString()
        goodsResource = parcel.readParcelable(GoodsResource::class.java.classLoader)
        originalPrice = parcel.readString()
        pictureUrls = parcel.readString()
        purchaseStatus = parcel.readInt()
        status = parcel.readInt()
        tagName = parcel.readString()
        thumbnailUrl = parcel.readString()
        freeFlag = parcel.readInt()
        discount = parcel.readFloat()
        discountPrice = parcel.readString()
        hotFlag = parcel.readInt()
        priceFlag = parcel.readInt()
        tagKey = parcel.readString()
        realPrice = parcel.readString()
        copyrightFlag = parcel.readInt()
        preferentialFlag = parcel.readInt()
        finalPrice = parcel.readString()
    }

    fun isProtected(): Boolean {
        return copyrightFlag != 0
    }

    fun hasDiscount(): Boolean {
        return preferentialFlag == 1
    }

    // 热卖
    fun isHotSales(): Boolean {
        return hotFlag == 1
    }

    // 爆款
    fun isBestSales(): Boolean {
        return hotFlag == 2
    }

    fun isMemberFree(): Boolean {
        return priceFlag == 2
    }

    fun goodsResourceIsValid(): Boolean {
        return goodsResource?.resFileUrl?.isNotEmpty() ?: false
    }

    fun isCreateNoteStatus(): Boolean {
        return purchaseStatus == 1
    }

    fun isPriceStatus(): Boolean {
        return purchaseStatus == 0
    }

    fun isFreeGoods(): Boolean {
        return priceFlag == 1
    }

    /**是否为免费商品*/
    fun isFreePrice(): Boolean {
        return freeFlag == 1
    }

    fun goodsId(): Int {
        return goodsId
    }

    fun getPayPrice(): String? {
        return finalPrice
//        return if (isMemberFree()) {
//            discountPrice
//        } else if (isFreeGoods()) {
//            discountPrice
//        } else if (hasDiscount()) {
//            discountPrice
//        } else {
//            realPrice
//        }
    }

    constructor(id: Int, tagName: String, goodsName: String, description: String, originalPrice: String, thumbnailUrl: String):this() {
        this.goodsId = id
        this.tagName = tagName
        this.goodsName = goodsName
        this.description = description
        this.originalPrice = originalPrice
        this.thumbnailUrl = thumbnailUrl
    }

    fun update(goodsInfo: MarketGoodsDetail) {
        goodsResource = goodsInfo.goodsResource
        purchaseStatus = goodsInfo.purchaseStatus
        originalPrice = goodsInfo.originalPrice
        discountPrice = goodsInfo.discountPrice
        realPrice = goodsInfo.realPrice
        discount = goodsInfo.discount
        freeFlag = goodsInfo.freeFlag
        hotFlag = goodsInfo.hotFlag
        copyrightFlag = goodsInfo.copyrightFlag
        priceFlag = goodsInfo.priceFlag
        finalPrice = goodsInfo.finalPrice
        preferentialFlag = goodsInfo.preferentialFlag
    }

    class GoodsResource() : Parcelable {
        // 资源文件url
        var resFileUrl: String? = ""
        // 缩略图
        var resThumbnailUrl: String? = ""
        // 资源类型（0:图片 1:PDF）
        var resType: Int = -1

        constructor(parcel: Parcel) : this() {
            resFileUrl = parcel.readString()
            resThumbnailUrl = parcel.readString()
            resType = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(resFileUrl)
            parcel.writeString(resThumbnailUrl)
            parcel.writeInt(resType)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun toString(): String {
            return "GoodsResource(resFileUrl=$resFileUrl, resThumbnailUrl=$resThumbnailUrl, resType=$resType)"
        }

        companion object CREATOR : Parcelable.Creator<GoodsResource> {
            override fun createFromParcel(parcel: Parcel): GoodsResource {
                return GoodsResource(parcel)
            }

            override fun newArray(size: Int): Array<GoodsResource?> {
                return arrayOfNulls(size)
            }
        }
    }



    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other is MarketGoods) {
            return goodsId == other.goodsId
        }
        return super.equals(other)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryKey)
        parcel.writeString(description)
        parcel.writeInt(goodsId)
        parcel.writeString(goodsName)
        parcel.writeString(goodsNo)
        parcel.writeParcelable(goodsResource, flags)
        parcel.writeString(originalPrice)
        parcel.writeString(pictureUrls)
        parcel.writeInt(purchaseStatus)
        parcel.writeInt(status)
        parcel.writeString(tagName)
        parcel.writeString(thumbnailUrl)
        parcel.writeInt(freeFlag)
        parcel.writeFloat(discount)
        parcel.writeString(discountPrice)
        parcel.writeInt(hotFlag)
        parcel.writeInt(priceFlag)
        parcel.writeString(tagKey)
        parcel.writeString(realPrice)
        parcel.writeInt(copyrightFlag)
        parcel.writeInt(preferentialFlag)
        parcel.writeString(finalPrice)
    }

    override fun toString(): String {
        return "MarketGoods(categoryKey=$categoryKey, description=$description, goodsId=$goodsId, goodsName=$goodsName, goodsNo=$goodsNo, goodsResource=$goodsResource, originalPrice=$originalPrice, pictureUrls=$pictureUrls, purchaseStatus=$purchaseStatus, status=$status, tagName=$tagName, thumbnailUrl=$thumbnailUrl, freeFlag=$freeFlag, discount=$discount, discountPrice=$discountPrice, hotFlag=$hotFlag, tagKey=$tagKey, realPrice=$realPrice, finalPrice=$finalPrice, preferentialFlag=$preferentialFlag, priceFlag=$priceFlag, copyrightFlag=$copyrightFlag)"
    }

    companion object CREATOR : Parcelable.Creator<MarketGoods> {
        override fun createFromParcel(parcel: Parcel): MarketGoods {
            return MarketGoods(parcel)
        }

        override fun newArray(size: Int): Array<MarketGoods?> {
            return arrayOfNulls(size)
        }
    }


}