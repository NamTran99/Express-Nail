package com.example.nailexpress.views.ui.main.customer.salon.adapter

import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemImageBinding
import com.example.nailexpress.models.ui.AppImage


interface IImageLocalAdapterAction {
    val onRemoveImage: ((url: AppImage) -> Unit)
}

class ImageLocalAdapter(
    val action: IImageLocalAdapterAction? = null,
    val status: AppConfig.Status = AppConfig.Status.UPDATE
) :
    SimpleRecyclerAdapter<AppImage, ItemImageBinding>() {

    override fun onBindHolder(item: AppImage, binding: ItemImageBinding, adapterPosition: Int) {
        binding.apply {
            image.enableRemoveImage = status == AppConfig.Status.UPDATE
            image.setImageUrl(item.image)
            image.setActionClearImage  { _ ->
                action?.onRemoveImage?.invoke(item)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.item_image

}