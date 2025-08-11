package com.abcnv.nvone.biz_https

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
interface IFileServiceApi {
    @POST("/upload")
    fun <T> upload(body: MultipartBody): Observable<ResponseBean<T>>
    @GET
    fun download(@Url httpFileUrl: String): Call<ResponseBody>

}