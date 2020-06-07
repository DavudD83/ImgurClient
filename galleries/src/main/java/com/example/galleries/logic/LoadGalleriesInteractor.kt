package com.example.galleries.logic

import com.example.galleries.di.GalleriesScope
import com.example.galleries.data.GalleriesService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.disposables.SequentialDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@GalleriesScope
class LoadGalleriesInteractor @Inject constructor(
    private val galleriesService: GalleriesService
) {
    lateinit var listener: Listener

    private var currPage = 0
    private var galleries = emptyList<Gallery>()

    private val mainDisposable = SequentialDisposable()
    private var loadGalleries = false

    fun start() {
        Flowable
            .concat(
                loadGalleriesInternal().toFlowable(),
                listener
                    .onNewPageRequested()
                    .filter { !loadGalleries }
                    .flatMapSingle { loadGalleriesInternal() }
            )
            .doOnError { listener.showLoadingError() }
            .onErrorResumeNext { Flowable.never() }
            .subscribe { newGalleries ->
                val updatedGalleries: List<Gallery> = galleries + newGalleries
                galleries = updatedGalleries

                listener.showGalleries(galleries)
            }
            .also { mainDisposable.update(it) }
    }

    fun stop() {
        mainDisposable.update(null)
    }

    private fun loadGalleriesInternal(): Single<List<Gallery>> {
        return galleriesService
            .loadViral(currPage)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadGalleries = true
                listener.showGalleriesLoading()
            }
            .doOnSuccess { currPage += 1 }
            .doFinally { loadGalleries = false }
    }

    interface Listener {
        fun showGalleriesLoading()
        fun showLoadingError()
        fun onNewPageRequested(): Flowable<Unit>
        fun showGalleries(galleries: List<Gallery>)
    }
}