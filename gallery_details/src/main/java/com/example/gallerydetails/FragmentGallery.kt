package com.example.gallerydetails

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.core_ui.dpToPx
import com.example.network.WithGalleriesApiProvider
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core.handleEvent
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.core_api.GoBackListener
import kotlinx.android.synthetic.main.fragment_gallery.*

class FragmentGallery : BaseFragment<GalleryVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_gallery
    override val vmClass: Class<GalleryVM>
        get() = GalleryVM::class.java

    lateinit var goBackListener: GoBackListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        goBackListener = parentFragment as GoBackListener

        val providersFacade = (requireActivity().applicationContext as AppWithFacade).facade()
        val galleriesApiProvider = (parentFragment as WithGalleriesApiProvider).provider()
        val galleryId = arguments!!.getString(EXTRA_GALLERY_ID_KEY)!!
        val name = arguments!!.getString(EXTRA_GALLERY_NAME_KEY)!!

        DaggerGalleryComponent.factory()
            .create(providersFacade, galleriesApiProvider, galleryId, name)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        images.adapter = ImagesAdapter()
        images.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val adapterPos = parent.getChildAdapterPosition(view)

                if (adapterPos != -1) {
                    outRect.set(0, dpToPx(8), 0,0)
                }
            }
        })

        arrowBack.setOnClickListener { goBackListener.goBack() }

        viewModel.images.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            (images.adapter as ImagesAdapter).submitList(it)
        })
        viewModel.title.observe(viewLifecycleOwner, Observer {
            title.text = it
        })
        viewModel.views.observe(viewLifecycleOwner, Observer {
            views.text = it
        })
        viewModel.date.observe(viewLifecycleOwner, Observer {
            date.text = it
        })
        viewModel.loading.handleEvent(viewLifecycleOwner) {
            loading.visibility = View.VISIBLE
        }
    }

    companion object {
        fun create(galleryId: String, galleryName: String): FragmentGallery {
            val bundle = Bundle().apply {
                putString(EXTRA_GALLERY_ID_KEY, galleryId)
                putString(EXTRA_GALLERY_NAME_KEY, galleryName)
            }

            return FragmentGallery().apply { arguments = bundle }
        }

        private const val EXTRA_GALLERY_ID_KEY = "EXTRA_GALLERY_ID_KEY"
        private const val EXTRA_GALLERY_NAME_KEY = "EXTRA_GALLERY_NAME_KEY"
    }
}