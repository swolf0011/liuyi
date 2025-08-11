package com.abcnv.nvone.lib_framework

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
/**
 * @Description:
 *
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
object NvRxJavaUtil {
    fun <T> hander(oos: ObservableOnSubscribe<T>, next: Consumer<T>) {
        Observable.create(oos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next)
    }

    fun <T> hander(
        oos: ObservableOnSubscribe<T>,
        next: Consumer<T>,
        error: Consumer<in Throwable?>?
    ) {
        Observable.create(oos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next, error)
    }

    fun <T> hander(oos: ObservableOnSubscribe<T>, observer: Observer<T>) {
        Observable.create(oos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
}