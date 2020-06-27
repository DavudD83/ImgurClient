package com.example.gallerydetails

import com.example.base_galleries.Gallery
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.internal.disposables.SequentialDisposable
import javax.inject.Inject

@GalleryScope
class LoadGalleryInteractor @Inject constructor(
    @GalleryId private val galleryId: String,
    private val singleGalleryService: SingleGalleryService
) {
    lateinit var listener: Listener

    private val disposable = SequentialDisposable()

    fun start() {
        stop()

        singleGalleryService
            .loadGalleryInfoBy(galleryId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { listener.showGalleryLoading() }
            .subscribe(
                listener::showGallery,
                listener::showLoadingError
            )
    }

    fun stop() {
        disposable.update(null)
    }

    interface Listener {
        fun showGalleryLoading()
        fun showGallery(gallery: Gallery)
        fun showLoadingError(throwable: Throwable)
    }
}