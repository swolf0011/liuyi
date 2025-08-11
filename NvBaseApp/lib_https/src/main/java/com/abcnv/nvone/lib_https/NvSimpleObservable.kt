package com.abcnv.nvone.lib_https

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class NvSimpleObservable<T> : Observer<T> {
    override fun onSubscribe(d: Disposable) {
    }
    override fun onComplete() {
    }
}