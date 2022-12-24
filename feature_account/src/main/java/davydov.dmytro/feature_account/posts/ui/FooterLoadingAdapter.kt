package davydov.dmytro.feature_account.posts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import davydov.dmytro.feature_account.databinding.ItemFooterLoadingBinding

class FooterLoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<FooterLoadingAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val holder = Holder(
            ItemFooterLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.binding.retryButton.setOnClickListener { retry() }
        return holder
    }

    override fun getStateViewType(loadState: LoadState): Int = LOADING_VIEW_TYPE

    class Holder(val binding: ItemFooterLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.loading.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        const val LOADING_VIEW_TYPE = 10
    }
}