package com.example.galleries.logic

import com.example.galleries.data.GalleriesService
import com.example.network.ConnectionStateService
import com.example.network.model.NetworkError
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoadGalleriesInteractorTest {

    lateinit var loadGalleriesInteractor: LoadGalleriesInteractor

    @Mock
    lateinit var galleriesService: GalleriesService

    @Mock
    lateinit var connectionStateService: ConnectionStateService

    @Mock
    lateinit var listener: LoadGalleriesInteractor.Listener

    @Rule
    @JvmField
    val rxRule = RxRule()

    private val onNewPageProcessor = PublishProcessor.create<Unit>()
    private val galleriesSubject = SingleSubject.create<List<Gallery>>()
    private val connectionStateProcessor = PublishProcessor.create<Boolean>()

    private val galleries = provideGalleries()

    @Before
    fun setUp() {
        loadGalleriesInteractor = LoadGalleriesInteractor(galleriesService, connectionStateService)
        loadGalleriesInteractor.listener = listener

        `when`(listener.onNewPageRequested()).thenReturn(onNewPageProcessor)
        `when`(galleriesService.loadViral(anyNonNull())).thenReturn(galleriesSubject)
        `when`(connectionStateService.trackConnectionState()).thenReturn(connectionStateProcessor)

        loadGalleriesInteractor.start()
    }

    @Test
    fun onStart_shouldLoadDataForZeroPage() {
        verify(galleriesService).loadViral(0)
    }

    @Test
    fun shouldShowLoading_givenDataLoading() {
        verify(listener).showGalleriesLoading()
    }

    @Test
    fun shouldShowData_givenDataLoaded() {
        galleriesSubject.onSuccess(galleries)

        verify(listener).showGalleries(galleries)
    }

    @Test
    fun shouldShowError_givenLoadingFailedNotWithNetworkError() {
        galleriesSubject.onError(Throwable())

        verify(listener).showLoadingError()
    }

    @Test
    fun shouldLoadDataOnConnection_givenLoadingFailedWithNetworkError() {
        galleriesSubject.onSuccess(galleries)

        val newGalleries = listOf(
            Gallery("dsada", "title", 1231, 3123, 3232, 2323, emptyList())
        )

        val expectedGalleries = galleries + newGalleries
        var firstSubscription = true

        `when`(galleriesService.loadViral(1)).thenReturn(Single.create {
            if (firstSubscription) {
                firstSubscription = false
                it.onError(NetworkError())
            } else {
                it.onSuccess(newGalleries)
            }
        })

        onNewPageProcessor.onNext(Unit)
        connectionStateProcessor.onNext(true)

        verify(listener).showGalleries(expectedGalleries)
    }

    @Test
    fun shouldNotLoadDataForNextPage_givenNewPageRequestedAndDataLoading() {
        onNewPageProcessor.onNext(Unit)

        verify(galleriesService, times(0)).loadViral(1)
    }

    @Test
    fun shouldLoadDataForNextPage_givenNewPageRequestedAndDataWasLoadedBefore() {
        galleriesSubject.onSuccess(galleries)
        onNewPageProcessor.onNext(Unit)

        verify(galleriesService).loadViral(1)
    }

    @Test
    fun shouldShowPreviousDataWithNewData_givenDataLoadedForNewPage() {
        val newGalleries =
            galleries.mapIndexed { index, gallery -> gallery.copy("${gallery.id}$index") }

        val expectedGalleries = galleries + newGalleries

        galleriesSubject.onSuccess(galleries)

        `when`(galleriesService.loadViral(1)).thenReturn(
            Single.just(
                newGalleries
            )
        )

        onNewPageProcessor.onNext(Unit)

        verify(listener).showGalleries(expectedGalleries)
    }

    @Test
    fun shouldRecoverFromError_givenLoadingFailedNotWithNetworkError() {
        galleriesSubject.onSuccess(galleries)

        var firstSubscription = true

        `when`(galleriesService.loadViral(1)).thenReturn(Single.create {
            if (firstSubscription) {
                firstSubscription = false
                it.onError(Throwable())
            } else {
                it.onSuccess(galleries)
            }
        })

        repeat(2) {
            onNewPageProcessor.onNext(Unit)
        }

        verify(galleriesService, times(2)).loadViral(1)
    }

    @Test
    fun shouldBeAbleToLoadNewPages_givenFirstLoadingFailedWithNotNetworkError() {
        galleriesSubject.onError(Throwable())

        onNewPageProcessor.onNext(Unit)

        verify(galleriesService, times(2)).loadViral(0)
    }

    @Test
    fun onStop_shouldDisposeCurrentRequest_givenDataLoading() {
        loadGalleriesInteractor.stop()

        galleriesSubject.onSuccess(galleries)

        verify(listener, times(0)).showGalleries(anyNonNull())
    }

    @Test
    fun onStop_shouldNotLoadNewData_givenNewPageRequested() {
        loadGalleriesInteractor.stop()

        galleriesSubject.onSuccess(galleries)
        onNewPageProcessor.onNext(Unit)

        verify(galleriesService, times(0)).loadViral(1)
    }
}