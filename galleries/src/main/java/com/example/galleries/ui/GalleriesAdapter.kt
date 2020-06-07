package com.example.galleries.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galleries.R
import davydov.dmytro.core.exhaustive
import kotlinx.android.synthetic.main.item_gallery.view.*

class GalleriesAdapter(private val viewHolderWidth: Int, private val viewHolderHeight: Int) :
    ListAdapter<GalleryListItem, GalleryListVH>(
        GalleryListDiffUtilCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryListVH {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return GalleryListVH(
            view,
            viewHolderWidth,
            viewHolderHeight
        )
    }

    override fun onBindViewHolder(holder: GalleryListVH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            NewPageLoading -> R.layout.item_gallery_loading
            is GalleryItem -> R.layout.item_gallery
        }
    }
}

object GalleryListDiffUtilCallback : DiffUtil.ItemCallback<GalleryListItem>() {
    override fun areContentsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem): Boolean {
        return when (oldItem) {
            NewPageLoading -> true
            is GalleryItem -> {
                oldItem == newItem as GalleryItem
            }
        }
    }

    override fun areItemsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem): Boolean {
        return when (oldItem) {
            NewPageLoading -> {
                newItem is NewPageLoading
            }
            is GalleryItem -> {
                if (newItem is GalleryItem) {
                    oldItem.id == newItem.id
                } else {
                    false
                }
            }
        }
    }
}

class GalleryListVH(itemView: View, private val widthImg: Int, private val heightImg: Int) :
    RecyclerView.ViewHolder(itemView) {

    init {
        itemView.image.updateLayoutParams {
            width = widthImg
            height = heightImg
        }
    }

    fun bind(galleryListItem: GalleryListItem) {
        when (galleryListItem) {
            NewPageLoading -> {
            }
            is GalleryItem -> bindGalleryItemInternally(galleryListItem)
        }.exhaustive
    }

    private fun bindGalleryItemInternally(galleryItem: GalleryItem) {
        itemView.run {
            title.text = galleryItem.title
            ups.text = galleryItem.ups
            downs.text = galleryItem.downs
            views.text = galleryItem.viewsStr

            val placeholderDrawable = resources.getDrawable(R.drawable.ic_image_placeholder)

            Glide.with(this)
                .load(galleryItem.pictureUrl)
                .placeholder(placeholderDrawable)
                .centerCrop()
                .into(image)
        }
    }
}