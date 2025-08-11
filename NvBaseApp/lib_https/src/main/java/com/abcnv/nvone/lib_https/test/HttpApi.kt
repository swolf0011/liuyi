package com.abcnv.nvone.lib_https.test

import android.util.Log
import com.abcnv.nvone.lib_https.NvRetrofitManagerUpload
import com.abcnv.nvone.lib_https.NvRetrofitManagerHttp
import com.abcnv.nvone.lib_https.NvSimpleObservable
import com.abcnv.nvone.lib_https.test.data.CoverPaperCategory
import com.abcnv.nvone.lib_https.test.data.DResp
import com.abcnv.nvone.lib_https.test.data.DUploadFile
import com.abcnv.nvone.lib_https.test.data.MarketGoodsBody
import com.abcnv.nvone.lib_https.test.data.MarketGoodsPage
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

object HttpApi {


    private val fileUrl = "https://djweb.jidete.com"
    private val retrofitManagerFile = NvRetrofitManagerUpload.getInstance(fileUrl)
    private val apiFileService = retrofitManagerFile.createServiceApi(ApiFileService::class.java)

    private val baseUrl = "https://jnotetest.jideos.com/"
    private val retrofitManagerHttp = NvRetrofitManagerHttp.getInstance(baseUrl)
    private val apiService = retrofitManagerHttp.createServiceApi(ApiService::class.java)

    fun uploadFile(filePath: String, token: String, observer: Observer<DResp<DUploadFile>>) {
        Log.d("0011==", "22currentThread==${Thread.currentThread().name}")
        Log.d("0011==", "token==${token}")
        val file = File(filePath)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
//        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
        val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val body = builder.addFormDataPart("file", filePath, requestBody).build()
        val observable = apiFileService.uploadFile(body, token)
        observable
            .subscribeOn(Schedulers.io())//防止rxjava oom,并且RetrofitServiceManager添加.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .observeOn(AndroidSchedulers.mainThread())//回调到主线程
            .subscribe(observer)
    }

      fun download(url:String ) : Call<ResponseBody> {
          return apiService.download(url)
      }

    ///---------------------------------


    fun searchCoverPaper(
        language: String,
        observable: NvSimpleObservable<ResponseObject<List<CoverPaperCategory>>>
    ) {
        apiService.searchCoverPaper(language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observable)
    }

    fun notificationCloudException(
        message: String,
        token: String,
        observable: NvSimpleObservable<ResponseObject<String>>
    ) {
        apiService.notificationCloudException(message, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observable)
    }

    fun getGoodsList(
        body: MarketGoodsBody,
        token: String,
        observable: NvSimpleObservable<ResponseObject<MarketGoodsPage>>
    ) {
        apiService.getGoodsList(body, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observable)
    }
}