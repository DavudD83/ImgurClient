package com.example.galleries.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.core_ui.*
import com.example.galleries.R
import com.example.galleries.di.DaggerGalleriesComponent
import com.example.network.WithGalleriesApiProvider
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core_api.AppWithFacade
import kotlinx.android.synthetic.main.fragment_galleries.*
import javax.inject.Inject
import kotlin.math.roundToInt

class GalleriesFragment : BaseFragment<GalleriesViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_galleries
    override val vmClass: Class<GalleriesViewModel>
        get() = GalleriesViewModel::class.java

    @Inject
    lateinit var recyclerPaginationHelper: RecyclerPaginationHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerGalleriesComponent.builder()
            .providersFacade((requireContext().applicationContext as AppWithFacade).facade())
            .galleriesApiProvider((parentFragment as WithGalleriesApiProvider).provider())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerPaginationHelper.subscribe(galleries, viewLifecycleOwner) {
            viewModel.galleriesListReachedEnd()
        }

        galleries.addItemDecoration(
            ItemDecorator(
                galleriesOffset
            )
        )
        galleries.layoutManager = GridLayoutManager(context, spanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val isLoading =
                        (galleries.adapter as GalleriesAdapter).currentList[position] is NewPageLoading
                    return if (isLoading) {
                        2
                    } else {
                        1
                    }
                }
            }
        }

        val lifecycleOfSizeCalculation = object : LifecycleOwner {
            private val lifecycleRegistry = LifecycleRegistry(this)

            override fun getLifecycle(): Lifecycle = lifecycleRegistry

            fun handleDimensionsCalculated() {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            }
        }

        val customViewLifecycle = LifecycleMerger.create(
            viewLifecycleOwner.lifecycle,
            lifecycleOfSizeCalculation.lifecycle
        )

        requireActivity().window.decorView.doOnApplyInsets { v, insets ->
            val typedValue = TypedValue()
            context!!.theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)

            v.setBackgroundColor(typedValue.data)

            val heightOfScreen =
                v.measuredHeight - insets.systemWindowInsetTop - insets.systemWindowInsetBottom

            val viewHolderWidth = v.measuredWidth / spanCount - itemOffset
            val viewHolderHeight = (heightOfScreen / verticalSpanCount).roundToInt() - itemOffset

            galleries.adapter = GalleriesAdapter(
                viewHolderWidth,
                viewHolderHeight
            )

            lifecycleOfSizeCalculation.handleDimensionsCalculated()

            insets
        }

        viewModel.galleries.observe(customViewLifecycle, Observer { items ->
            loading.isVisible = false
            galleries.isVisible = true
            (galleries.adapter as GalleriesAdapter).submitList(items)
        })
        viewModel.showInitialLoading.observe(viewLifecycleOwner, Observer {
            galleries.isVisible = false
            initialError.isVisible = false
            loading.isVisible = true
        })
        viewModel.showInitialLoadingError.observe(viewLifecycleOwner, Observer {
            loading.isVisible = false
            initialError.isVisible = true
            initialError.text = it
        })
    }

    override fun onDestroyView() {
        requireActivity().window.decorView.setOnApplyWindowInsetsListener(null)
        super.onDestroyView()
    }

    companion object {
        private val galleriesOffset = dpToPx(4)
        private val itemOffset = galleriesOffset * 2
        private const val verticalSpanCount = 3.0
        private const val spanCount = 2
    }
}