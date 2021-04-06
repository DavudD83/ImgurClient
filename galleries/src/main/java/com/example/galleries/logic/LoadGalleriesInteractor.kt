package com.example.galleries.logic

import com.example.galleries.data.GalleriesService
import com.example.galleries.di.GalleriesScope
import com.example.network.ConnectionStateService
import com.example.network.model.NetworkError
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.internal.disposables.SequentialDisposable
import javax.inject.Inject

@GalleriesScope
class LoadGalleriesInteractor @Inject constructor(
    private val galleriesService: GalleriesService,
    private val connectionStateService: ConnectionStateService
) {
    lateinit var listener: Listener

    private var currPage = 0
    private var galleries = emptyList<Gallery>()

    private val mainDisposable = SequentialDisposable()
    private var loadGalleries = false

    fun start() {
        Flowable
            .concat(
                loadGalleriesInternal(),
                listener
                    .onNewPageRequested()
                    .filter { !loadGalleries }
                    .flatMap { loadGalleriesInternal() }
            )
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

    private fun loadGalleriesInternal(): Flowable<List<Gallery>> {
        return galleriesService
            .loadViral(currPage)
            .retryWhen { errorFlowable ->
                errorFlowable
                    .flatMap { error ->
                        if (error is NetworkError) {
                            connectionStateService
                                .trackConnectionState()
                                .filter { it }
                        } else {
                            Flowable.error(error)
                        }
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadGalleries = true
                listener.showGalleriesLoading()
            }
            .doOnSuccess { currPage += 1 }
            .doOnError { listener.showLoadingError() }
            .doFinally { loadGalleries = false }
            .toFlowable()
            .onErrorComplete()
    }

    interface Listener {
        fun showGalleriesLoading()
        fun showLoadingError()
        fun onNewPageRequested(): Flowable<Unit>
        fun showGalleries(galleries: List<Gallery>)
    }
}