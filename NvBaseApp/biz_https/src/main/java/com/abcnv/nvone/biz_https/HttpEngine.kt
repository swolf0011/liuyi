package com.abcnv.nvone.biz_https

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object HttpEngine {
    var token = ""


    private val mServiceApi: IServiceApi =
        RetrofitManagerUtil.getInstance().retrofitManager.createServiceApi(IServiceApi::class.java)
    private val mFileServiceApi: IFileServiceApi =
        RetrofitManagerUtil.getInstance().retrofitManager.createServiceApi(IFileServiceApi::class.java)

    private fun <T> setSubscribe(observable: Observable<T>, resultCallback: IResultCallback<T>) {
        observable
            //.subscribeOn(Schedulers.io())//防止rxjava oom,并且NyRetrofitManager添加.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .observeOn(AndroidSchedulers.mainThread()) //回调到主线程
            .subscribe(object : Observer<T> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    println("0011==onError:${e.printStackTrace()}")
                    resultCallback.onFail(e.printStackTrace().toString())
                }

                override fun onComplete() {
                }

                override fun onNext(t: T) {
                    println("0011==onNext:${t}")
                    resultCallback.onSuccess(t)
                }
            })
    }

    private fun <T> setSubscribeSync(
        observable: Observable<T>,
        resultCallback: IResultCallback<T>
    ) {
        observable.subscribe(object : Observer<T> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                println("0011==onError:${e.printStackTrace()}")
                resultCallback.onFail(e.printStackTrace().toString())
            }

            override fun onComplete() {
            }

            override fun onNext(t: T) {
                println("0011==onNext:${t}")
                resultCallback.onSuccess(t)
            }
        })
    }

    interface IResultCallback<T> {
        fun onFail(errMsg:String)
        fun onSuccess(t: T)
    }


    /**
     * 登录
     * @param phone String
     * @param code String
     * @param observer Observer<ResponseBean<Object>>
     */
    fun loginSms(
        phone: String,
        code: String,
        observer: IResultCallback<ResponseBean<Object>>
    ) {
        val json = "{\"phone\":\"${phone}\",\"code\":\"${code}\",}"
        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)
        setSubscribe(mServiceApi.loginSms(requestBody), observer)
    }

}