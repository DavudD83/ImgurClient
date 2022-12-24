package davydov.dmytro.feature_account.posts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core_ui.loadImage
import davydov.dmytro.feature_account.databinding.ItemPostBinding

class PostsAdapter : PagingDataAdapter<PostUiState, PostsAdapter.Holder>(PostsDiffItemCallback) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class Holder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(postUiState: PostUiState) {
            binding.run {
                image.loadImage(postUiState.coverUrl)
                title.text = postUiState.title
                privacyStatus.text = postUiState.privacyString
                severalImagesGroup.isVisible = postUiState.multipleImagesInsideVisible
                imagesCount.text = postUiState.imagesCountString
            }
        }
    }

    object PostsDiffItemCallback : DiffUtil.ItemCallback<PostUiState>() {
        override fun areItemsTheSame(oldItem: PostUiState, newItem: PostUiState): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PostUiState, newItem: PostUiState): Boolean =
            oldItem == newItem
    }
}