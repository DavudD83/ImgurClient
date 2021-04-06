package com.example.galleries.ui

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.galleries.*
import com.example.galleries.logic.Gallery
import com.example.galleries.logic.LoadGalleriesInteractor
import davydov.dmytro.core.Event
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnitParamsRunner::class)
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
        MockitoAnnotations.initMocks(this)

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

        verify(galleriesObserver).onChanged(argThat(PictureUrlMatcher(galleries)))
    }

    @Test
    @Parameters(
        value = ["1000, $thousands", "1000000, $millions"]
    )
    fun shouldShowNumbersCorrectly_givenDifferentCount(numbers: Int, expectedStr: String) {
        val galleries =
            listOf(Gallery("d", "title", numbers, numbers, numbers, 130, provideImages()))

        val expected = galleries.map {
            GalleryItem(
                it.id,
                it.title,
                it.images.first().url,
                expectedStr,
                expectedStr,
                expectedStr
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
        clearInvocations(galleriesObserver)

        viewModel.showLoadingError()

        verify(galleriesObserver).onChanged(galleryItems)
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

    companion object {
        private const val thousands = "thousands"
        private const val millions = "millions"
    }
}