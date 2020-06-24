package com.example.galleries.ui

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.galleries.*
import com.example.galleries.logic.Gallery
import com.example.galleries.logic.LoadGalleriesInteractor
import davydov.dmytro.core.Event
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GalleriesViewModelTest {

    lateinit var viewModel: GalleriesViewModel

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var loadGalleriesInteractor: LoadGalleriesInteractor

    @Mock
    lateinit var showInitialLoadingObserver: Observer<Event<Unit>>

    @Mock
    lateinit var galleriesObserver: Observer<List<GalleryListItem>>

    @Mock
    lateinit var initialLoadingErrorObserver: Observer<String>

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    private val thousands = "thousands"
    private val millions = "millions"

    private val galleries = provideGalleries()

    private val galleryItems = galleries.map {
        GalleryItem(
            it.id,
            it.title,
            it.images.first().url,
            it.upVotes.toString(),
            it.downVotes.toString(),
            it.views.toString()
        )
    }

    @Before
    fun setUp() {
        viewModel = GalleriesViewModel(resources, loadGalleriesInteractor)

        viewModel.showInitialLoading.observeForever(showInitialLoadingObserver)
        viewModel.showInitialLoadingError.observeForever(initialLoadingErrorObserver)
        viewModel.galleries.observeForever(galleriesObserver)

        `when`(resources.getString(notNullEq(R.string.thousandsViewsStr), anyNonNull())).thenReturn(
            thousands
        )
        `when`(resources.getString(notNullEq(R.string.millionsViewStr), anyNonNull())).thenReturn(
            millions
        )
    }

    @Test
    fun onViewCreated_shouldInvokeStart() {
        viewModel.onViewCreated()

        verify(loadGalleriesInteractor).start()
    }

    @Test
    fun onViewDestroyed_shouldInvokeStop() {
        viewModel.onViewDestroyed()

        verify(loadGalleriesInteractor).stop()
    }

    @Test
    fun onShowGalleriesLoading_shouldShowInitialLoading_givenNoGalleries() {
        viewModel.showGalleriesLoading()

        verify(showInitialLoadingObserver).onChanged(Event(Unit))
    }

    @Test
    fun shouldShowGalleryItems_givenShowGalleries() {
        viewModel.showGalleries(galleries)

        verify(galleriesObserver).onChanged(anyNonNull())
    }

    @Test
    fun shouldTakeFirstImageUrlForItem_givenShowGalleries() {
        viewModel.showGalleries(galleries)

        verify(galleriesObserver).onChanged(notNullArgThat { result ->
            result.map { (it as GalleryItem).pictureUrl } == galleries.map { it.images.first().url }
        })
    }

    @Test
    fun shouldShowCountsAsThousands_givenCountBiggerThan1000() {
        val galleries = listOf(Gallery("d", "title", 1001, 1001, 1001, 130, provideImages()))

        val expected = galleries.map {
            GalleryItem(
                it.id,
                it.title,
                it.images.first().url,
                thousands,
                thousands,
                thousands
            )
        }

        viewModel.showGalleries(galleries)

        verify(galleriesObserver).onChanged(expected)
    }

    @Test
    fun shouldShowCountsAsMillions_givenCountBiggerThan1_000_000() {
        val galleries =
            listOf(Gallery("d", "title", 1_000_000, 1_000_000, 1_000_000, 130, provideImages()))

        val expected = galleries.map {
            GalleryItem(
                it.id,
                it.title,
                it.images.first().url,
                millions,
                millions,
                millions
            )
        }

        viewModel.showGalleries(galleries)

        verify(galleriesObserver).onChanged(expected)
    }

    @Test
    fun onShowGalleriesLoading_shouldShowGalleriesWithLoading_givenGalleriesExist() {
        val expectedGalleries = listOf(*galleryItems.toTypedArray(), NewPageLoading)

        viewModel.showGalleries(galleries)
        viewModel.showGalleriesLoading()

        verify(galleriesObserver).onChanged(expectedGalleries)
    }

    @Test
    fun onShowLoadingError_shouldShowInitialError_givenNoGalleries() {
        val expected = "error"
        `when`(resources.getString(R.string.initial_error)).thenReturn(expected)

        viewModel.showLoadingError()

        verify(initialLoadingErrorObserver).onChanged(expected)
    }

    @Test
    fun onShowLoadingError_shouldShowGalleriesWithoutLoading_givenGalleriesExist() {
        viewModel.showGalleries(galleries)
        viewModel.showLoadingError()

        verify(galleriesObserver, times(2)).onChanged(galleryItems)
    }

    @Test
    fun onGalleriesReachedEnd_shouldEmitRequestNewPage_givenNoNewPageError() {
        val testSubscriber = viewModel.onNewPageRequested().test()

        viewModel.galleriesListReachedEnd()

        testSubscriber.assertValue(Unit)
    }

    @Test
    fun onGalleriesReachedEnd_shouldNotEmitRequestNewPage_givenNewPageError() {
        val testSubscriber = viewModel.onNewPageRequested().test()

        viewModel.showGalleries(galleries)
        viewModel.showLoadingError()

        viewModel.galleriesListReachedEnd()

        testSubscriber.assertEmpty()
    }
}