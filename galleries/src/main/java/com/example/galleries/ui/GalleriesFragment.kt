package com.example.galleries.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.core_ui.ItemDecorator
import com.example.core_ui.doOnApplyInsets
import com.example.core_ui.dpToPx
import com.example.galleries.R
import com.example.galleries.di.DaggerGalleriesComponent
import com.example.network.WithGalleriesApiProvider
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core_api.AppWithFacade
import kotlinx.android.synthetic.main.fragment_galleries.*
import kotlin.math.roundToInt

class GalleriesFragment : BaseFragment<GalleriesViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_galleries
    override val vmClass: Class<GalleriesViewModel>
        get() = GalleriesViewModel::class.java

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

        galleries.addItemDecoration(
            ItemDecorator(
                galleriesOffset
            )
        )
        galleries.layoutManager = GridLayoutManager(context, spanCount)

        requireActivity().window.decorView.doOnApplyInsets { v, insets ->
            val heightOfScreen = v.height - insets.systemWindowInsetTop - insets.systemWindowInsetBottom

            val viewHolderWidth = v.measuredWidth / spanCount - itemOffset
            val viewHolderHeight = (heightOfScreen / verticalSpanCount).roundToInt() - itemOffset

            galleries.adapter = GalleriesAdapter(
                viewHolderWidth,
                viewHolderHeight
            )

            insets
        }

        viewModel.galleries.observe(viewLifecycleOwner, Observer { items ->
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