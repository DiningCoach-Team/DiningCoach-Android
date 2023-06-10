package com.dining.coach.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dining.coach.databinding.RowGalleryImageBinding
import com.diningcoach.domain.model.Gallery

class GalleryRecyclerViewAdapter :
    PagingDataAdapter<Gallery, GalleryRecyclerViewAdapter.ImageViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Gallery>() {
            override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery) =
                oldItem.uri == newItem.uri

            override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder = ImageViewHolder(
        RowGalleryImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class ImageViewHolder(
        private val binding: RowGalleryImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Gallery) {
            binding.run {
                Glide.with(binding.root.context)
                    .load(item.uri)
                    .into(binding.rowGalleryImageView)
            }
        }
    }
}
