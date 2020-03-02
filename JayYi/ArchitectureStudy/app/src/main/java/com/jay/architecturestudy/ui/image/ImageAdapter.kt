package com.jay.architecturestudy.ui.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jay.architecturestudy.databinding.ListItemImageBinding
import com.jay.architecturestudy.ui.BaseAdapter
import com.jay.architecturestudy.ui.BaseViewHolder
import com.jay.architecturestudy.ui.OnItemClickListener
import com.jay.architecturestudy.util.startWebView
import com.jay.repository.model.Image

internal class ImageAdapter : BaseAdapter<Image, ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = ListItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageHolder(binding)
    }

}

internal class ImageHolder(
    val binding: ListItemImageBinding
) : BaseViewHolder<Image>(binding), OnItemClickListener {

    init {
        binding.clickEvent = this
    }

    override fun bind(item: Image) {
        with(binding) {
            title = item.title
            imageUrl = item.thumbnail
            url = item.link
            executePendingBindings()
        }
    }

    override fun onClick(v: View, url: String) {
        v.startWebView(url)
    }
}