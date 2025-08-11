package com.abcnv.nvone.lib_https.test

import com.abcnv.nvone.lib_https.test.data.DResp
import com.abcnv.nvone.lib_https.test.data.DUploadFile
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiFileService {
    /**
     *
     * @param body RequestBody
     * @param token String "Bearer 58a0aab8e77a403dbda6950d21dde0f2"
     * @return Call<DResp<DUploadFile>>
     */
    @POST("/web-api/file/upload/")
    fun uploadFileTest(
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Observable<DResp<DUploadFile>>


    @POST("/web-api/file/upload/")
    fun uploadFile(
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Observable<DResp<DUploadFile>>
}