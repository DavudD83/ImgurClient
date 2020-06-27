package com.example.gallerydetails

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base_galleries.Gallery
import com.example.base_galleries.Image
import davydov.dmytro.core.BaseViewModel
import davydov.dmytro.core.Event
import davydov.dmytro.core.sendEvent
import java.text.SimpleDateFormat
import javax.inject.Inject

class GalleryVM @Inject constructor(
    private val loadGalleryInteractor: LoadGalleryInteractor,
    private val resources: Resources,
    @GalleryName private val galleryName: String
) : BaseViewModel(),
    LoadGalleryInteractor.Listener {

    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>> = _images

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _views = MutableLiveData<String>()
    val views: LiveData<String> = _views

    private val _loading = MutableLiveData<Event<Unit>>()
    val loading: LiveData<Event<Unit>> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val simpleDateFormat = SimpleDateFormat("dd MMM kk:mm")

    override fun onViewCreated() {
        super.onViewCreated()
        _title.value = galleryName
        loadGalleryInteractor.start()
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        loadGalleryInteractor.stop()
    }

    override fun showGalleryLoading() {
        _loading.sendEvent()
    }

    override fun showGallery(gallery: Gallery) {
        _date.value = resources.getString(R.string.date, simpleDateFormat.format(gallery.date))
        _views.value = resources.getString(R.string.views, gallery.views.toString())
        _images.value = gallery.images
    }

    override fun showLoadingError(throwable: Throwable) {
        _error.value = resources.getString(R.string.gallery_loading_error)
    }
}