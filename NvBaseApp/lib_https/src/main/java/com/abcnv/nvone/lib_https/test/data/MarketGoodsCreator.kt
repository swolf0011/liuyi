package com.abcnv.nvone.lib_https.test.data

import android.os.Parcel
import android.os.Parcelable

/**
 * @author: haosl
 * on: 2024/1/9
 * 作者信息
 */
class MarketGoodsCreator() : Parcelable {
    var id: Int = 0
    var creatorName: String? = ""
    var creatorDesc: String? = ""
    var avatarUrl: String? = ""
    // 关联商品数量
    var goodsNumber: Int = 0

    constructor(id: Int, name: String, des: String, iconUrl: String):this() {
        this.id = id
        creatorName = name
        creatorDesc = des
        this.avatarUrl = iconUrl
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        avatarUrl = parcel.readString()
        creatorDesc = parcel.readString()
        creatorName = parcel.readString()
        goodsNumber = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(avatarUrl)
        parcel.writeString(creatorDesc)
        parcel.writeString(creatorName)
        parcel.writeInt(goodsNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarketGoodsCreator> {
        override fun createFromParcel(parcel: Parcel): MarketGoodsCreator {
            return MarketGoodsCreator(parcel)
        }

        override fun newArray(size: Int): Array<MarketGoodsCreator?> {
            return arrayOfNulls(size)
        }
    }
}