package com.randomusers.common

import androidx.lifecycle.ViewModel
import com.domain.scheduler.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    protected fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun dispose() = disposables.dispose()

    protected fun <T> applySchedulersSingle(scheduler: SchedulerProvider): SingleTransformer<T, T> {
        return SingleTransformer { upstream: Single<T>? ->
            upstream!!.subscribeOn(scheduler.io()).observeOn(scheduler.mainThread())
        }
    }

    protected fun <T> applySchedulersObservable(
        scheduler: SchedulerProvider
    ): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T>? ->
            upstream!!.subscribeOn(scheduler.io()).observeOn(scheduler.mainThread())
        }
    }
}
