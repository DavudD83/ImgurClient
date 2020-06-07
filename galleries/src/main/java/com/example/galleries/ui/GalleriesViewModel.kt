package com.example.galleries.ui

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.galleries.R
import com.example.galleries.logic.Gallery
import com.example.galleries.logic.LoadGalleriesInteractor
import davydov.dmytro.core.BaseViewModel
import davydov.dmytro.core.Event
import davydov.dmytro.core.sendEvent
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import javax.inject.Inject

class GalleriesViewModel @Inject constructor(
    private val resources: Resources,
    private val loadGalleriesInteractor: LoadGalleriesInteractor
) : BaseViewModel(),
    LoadGalleriesInteractor.Listener {

    private val _galleries = MutableLiveData<List<GalleryListItem>>()
    val galleries: LiveData<List<GalleryListItem>> = _galleries

    private val galleriesCount: Int?
        get() = _galleries.value?.size

    private val _showInitialLoading = MutableLiveData<Event<Unit>>()
    val showInitialLoading: LiveData<Event<Unit>> = _showInitialLoading

    private val _showInitialLoadingError = MutableLiveData<String>()
    val showInitialLoadingError: LiveData<String> = _showInitialLoadingError

    private val onNewPageRequestedProcessor = PublishProcessor.create<Unit>()

    private var newPageError: Boolean = false

    override fun onViewCreated() {
        super.onViewCreated()
        loadGalleriesInteractor.start()
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        loadGalleriesInteractor.stop()
    }

    override fun showGalleriesLoading() {
        newPageError = false

        if (galleriesCount != null) {
            val galleriesWithPageLoading = _galleries.value!! + NewPageLoading
            _galleries.value = galleriesWithPageLoading
        } else {
            _showInitialLoading.sendEvent()
        }
    }

    override fun showLoadingError() {
        if (galleriesCount != null) {
            newPageError = true
            _galleries.value = _galleries.value!! - NewPageLoading
        } else {
            _showInitialLoadingError.value = resources.getString(R.string.initial_error)
        }
    }

    override fun onNewPageRequested(): Flowable<Unit> = onNewPageRequestedProcessor

    override fun showGalleries(galleries: List<Gallery>) {
        val galleryItems = galleries.map { gallery ->
            gallery.run {
                val coverImageUrl = images.first().url
                GalleryItem(
                    id,
                    title,
                    coverImageUrl,
                    upVotes.toString(),
                    downVotes.toString(),
                    getViewStrFor(views)
                )
            }
        }

        _galleries.value = galleryItems
    }

    fun galleriesListReachedEnd() {
        if (!newPageError) {
            onNewPageRequestedProcessor.onNext(Unit)
        }
    }

    private fun getViewStrFor(count: Int): String {
        return when {
            count >= 1_000_000 -> resources.getString(R.string.millionsViewStr, count / 1_000_000)
            count >= 1000 -> resources.getString(R.string.thousandsViewsStr, count / 1000)
            else -> count.toString()
        }
    }
}

sealed class GalleryListItem

data class GalleryItem(
    val id: String,
    val title: String,
    val pictureUrl: String,
    val ups: String,
    val downs: String,
    val viewsStr: String
) : GalleryListItem()

object NewPageLoading : GalleryListItem()