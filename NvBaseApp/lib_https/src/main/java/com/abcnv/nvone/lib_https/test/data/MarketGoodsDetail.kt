package com.abcnv.nvone.lib_https.test.data

import android.os.Parcel
import android.os.Parcelable

/**
 *  @Author: huanggb
 *  Date: 2023/12/5 14:15
 *
 * description	string
 * 商品描述
 * goodsId	integer($int32)
 * 商品ID
 * goodsName	string
 * 商品名称
 * goodsNo	string
 * 商品编号
 * goodsResource	GoodsResource
 * originalPrice	number
 * 商品原始价格
 * pictureUrls	string
 * 图片url，多个逗号分隔
 * purchaseStatus	integer($int32)
 * 购买状态 NULL：未购买 1：已购买 2：已创建
 * status	integer($int32)
 * 商品状态 0：上架，1：下架
 * tagName	string
 * 标签名称
 * thumbnailUrl	string
 * 缩略图url
 */
class MarketGoodsDetail() : Parcelable {

    var description: String? = ""

    var goodsId: String? = ""

    var goodsName: String?= ""

    var goodsNo: String? = ""

    var originalPrice: String? = ""

    var pictureUrlList = mutableListOf<String>()

    /**
     * 0  显示价格
     * 1  显示   创建
     * 2  显示  "已创建"
     */
    var purchaseStatus: Int = 0 //

    var status: Int= 0

    var tagName:  String?= ""

    var thumbnailUrl:  String? = ""
    // 标签标识key
    var tagKey:  String? = ""

    var realPrice:  String? = ""
    var discountPrice:  String? = ""
    var discount:  Float = 0.0f
    var freeFlag: Int = 0
    var hotFlag: Int = 0
    // 版权标识 0：无限制 1：保密级别高，不可导出、备份、上传
    var copyrightFlag: Int = 0

    var priceFlag: Int = 0

    // 商品的最终售价
    var finalPrice: String? = ""

    // 商品优惠标识 0：无优惠 1：折扣优惠
    var preferentialFlag: Int = 0

    var goodsResource: MarketGoods.GoodsResource? = null
    var goodsCreator: MarketGoodsCreator? = null
    var goodsDetail: ParamDetail? = null

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()
        goodsId = parcel.readString()
        goodsName = parcel.readString()
        goodsNo = parcel.readString()
        originalPrice = parcel.readString()
        purchaseStatus = parcel.readInt()
        status = parcel.readInt()
        tagName = parcel.readString()
        thumbnailUrl = parcel.readString()
        tagKey = parcel.readString()
        realPrice = parcel.readString()
        discountPrice = parcel.readString()
        discount = parcel.readFloat()
        freeFlag = parcel.readInt()
        hotFlag = parcel.readInt()
        copyrightFlag = parcel.readInt()
        priceFlag = parcel.readInt()
        preferentialFlag = parcel.readInt()
        finalPrice = parcel.readString()

        goodsResource = parcel.readParcelable(MarketGoods.GoodsResource::class.java.classLoader)
        goodsCreator = parcel.readParcelable(MarketGoodsCreator::class.java.classLoader)
        goodsDetail = parcel.readParcelable(ParamDetail::class.java.classLoader)
    }

    fun isCreateNoteStatus(): Boolean {
        return purchaseStatus == 1
    }

    fun isPriceStatus(): Boolean {
        return purchaseStatus == 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(goodsId)
        parcel.writeString(goodsName)
        parcel.writeString(goodsNo)
        parcel.writeString(originalPrice)
        parcel.writeInt(purchaseStatus)
        parcel.writeInt(status)
        parcel.writeString(tagName)
        parcel.writeString(thumbnailUrl)
        parcel.writeString(tagKey)
        parcel.writeString(realPrice)
        parcel.writeString(discountPrice)
        parcel.writeFloat(discount)
        parcel.writeInt(freeFlag)
        parcel.writeInt(hotFlag)
        parcel.writeInt(copyrightFlag)
        parcel.writeInt(priceFlag)
        parcel.writeInt(preferentialFlag)
        parcel.writeString(finalPrice)
        parcel.writeParcelable(goodsResource, flags)
        parcel.writeParcelable(goodsCreator, flags)
        parcel.writeParcelable(goodsDetail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "MarketGoodsDetail(description=$description, goodsId=$goodsId, goodsName=$goodsName, goodsNo=$goodsNo, originalPrice=$originalPrice, pictureUrlList=$pictureUrlList, purchaseStatus=$purchaseStatus, status=$status, tagName=$tagName, thumbnailUrl=$thumbnailUrl, tagKey=$tagKey, realPrice=$realPrice, discountPrice=$discountPrice, discount=$discount, freeFlag=$freeFlag, hotFlag=$hotFlag, copyrightFlag=$copyrightFlag, priceFlag=$priceFlag, finalPrice=$finalPrice, preferentialFlag=$preferentialFlag, goodsResource=$goodsResource, goodsCreator=$goodsCreator, goodsDetail=$goodsDetail)"
    }

    companion object CREATOR : Parcelable.Creator<MarketGoodsDetail> {
        override fun createFromParcel(parcel: Parcel): MarketGoodsDetail {
            return MarketGoodsDetail(parcel)
        }

        override fun newArray(size: Int): Array<MarketGoodsDetail?> {
            return arrayOfNulls(size)
        }
    }


    data class ParamDetail(
        var id: Int, //主键ID
        var creatorId: Int,//创作者ID
        var goodsId: Int, // 商品ID
        var layoutKey: String?,  // 布局key
        var layoutName: String?, // 布局名称
        var layout: String?, // 布局
        var specs: String?,   // 规格
        var pageNumber: Int, //页数
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeInt(creatorId)
            parcel.writeInt(goodsId)
            parcel.writeString(layoutKey)
            parcel.writeString(layoutName)
            parcel.writeString(layout)
            parcel.writeString(specs)
            parcel.writeInt(pageNumber)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun toString(): String {
            return "ParamDetail(id=$id, creatorId=$creatorId, goodsId=$goodsId, layoutKey=$layoutKey, layoutName=$layoutName, layout=$layout, specs=$specs, pageNumber=$pageNumber)"
        }

        companion object CREATOR : Parcelable.Creator<ParamDetail> {
            override fun createFromParcel(parcel: Parcel): ParamDetail {
                return ParamDetail(parcel)
            }

            override fun newArray(size: Int): Array<ParamDetail?> {
                return arrayOfNulls(size)
            }
        }
    }
}