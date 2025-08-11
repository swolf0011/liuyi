package com.abcnv.nvone.lib_https.test

import com.abcnv.nvone.lib_https.test.data.CoverPaperCategory
import com.abcnv.nvone.lib_https.test.data.MarketGoodsBody
import com.abcnv.nvone.lib_https.test.data.MarketGoodsPage
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("search/cover/paper?type=2")
    fun searchCoverPaper(@Query("language") language: String): Observable<ResponseObject<List<CoverPaperCategory>>>

    @POST("/notification/cloud")
    fun notificationCloudException(
        @Body message: String,
        @Header("token") token: String
    ): Observable<ResponseObject<String>>


    // 集市-商品列表
    @POST("/mall/goods/list")
    fun getGoodsList(
        @Body body: MarketGoodsBody,
        @Header("token") token: String
    ): Observable<ResponseObject<MarketGoodsPage>>


    //下载
    @GET
    fun download(@Url fileUrl: String): Call<ResponseBody>
}