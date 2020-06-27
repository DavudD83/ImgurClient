package com.example.gallerydetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.base_galleries.Image
import kotlinx.android.synthetic.main.item_image.view.*

class ImagesAdapter : ListAdapter<Image, ImagesAdapter.ImageHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageHolder(imageView: View) : RecyclerView.ViewHolder(imageView) {
        private val imageView = itemView.image

        fun bind(image: Image) {
            Glide.with(imageView)
                .load(image.url)
                .fitCenter()
                .into(imageView as ImageView)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Image>() {
            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}