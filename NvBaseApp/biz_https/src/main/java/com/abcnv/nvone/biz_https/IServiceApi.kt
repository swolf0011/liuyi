package com.abcnv.nvone.biz_https

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
/**
 * @Description
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
interface IServiceApi {
    @POST("/login")
    fun loginSms(@Body body: RequestBody): Observable<ResponseBean<Object>>
}