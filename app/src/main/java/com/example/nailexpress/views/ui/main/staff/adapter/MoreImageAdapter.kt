package com.example.nailexpress.views.ui.main.staff.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.support.core.view.*
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.nailexpress.app.AppConfig

import com.example.nailexpress.databinding.ItemMoreImageBinding
import com.example.nailexpress.databinding.LayoutBtnAddPictureBinding
import com.example.nailexpress.extension.findIndex
import com.example.nailexpress.models.ui.AppImage


interface IMoreImageAdapter{
    var onAddImagesAction: (() -> Unit)
    var onRemoveImageAction: ((AppImage) -> Unit)
    var onItemClickListener: ((String) -> Unit)
}

class MoreImageAdapter(val action: IMoreImageAdapter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ADD_IMAGE = 0
        const val TYPE_IMAGE = 1
    }

    private var mItems = ArrayList<AppImage>()

    init {
        mItems.add(AppImage())
    }

    val items get() = mItems
    var listImage = listOf<AppImage>()

    @SuppressLint("NotifyDataSetChanged")
    fun submit(listImage: List<AppImage>?){
        if(listImage.isNullOrEmpty()) return
        mItems.clear()
        this@MoreImageAdapter.listImage = listImage?: listOf()
        mItems.add(AppImage())
        mItems.addAll(listImage)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        mItems = ArrayList(mItems.dropLast(mItems.size - 1))
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADD_IMAGE ->
                AddImageViewHolder(parent.bindingOf(LayoutBtnAddPictureBinding::inflate))
            TYPE_IMAGE ->
                ImageViewHolder(parent.bindingOf(ItemMoreImageBinding::inflate))
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_ADD_IMAGE else TYPE_IMAGE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderExtension<AppImage>).bind(items[position])
    }

    override fun getItemCount(): Int = mItems.size

    inner class AddImageViewHolder(override val binding: LayoutBtnAddPictureBinding) :
        BaseViewHolder<LayoutBtnAddPictureBinding>(binding), ViewHolderExtension<AppImage> {
        override fun bind(item: AppImage) {
            binding.root.setOnClickListener {
                action.onAddImagesAction.invoke()
            }
        }
    }

    inner class ImageViewHolder(override val binding: ItemMoreImageBinding) :
        BaseViewHolder<ItemMoreImageBinding>(binding), ViewHolderExtension<AppImage> {
        @RequiresApi(Build.VERSION_CODES.Q)
        @SuppressLint("CheckResult", "ResourceAsColor")
        override fun bind(item: AppImage) {
            binding.apply {
                image.setImageStatus(AppConfig.Status.UPDATE)
                image.enableRemoveImage = true
                image.setImageUrl(item.image)
                image.setActionClearImage{
                    action.onRemoveImageAction.invoke(item)
                    removeItem(item)
                }
                root.setOnClickListener {
                    action.onItemClickListener.invoke(item.image)
                }
            }
        }
    }

    fun removeItem(path: AppImage) {
        val index = mItems.findIndex { it == path }
        if (index > -1) {
            mItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}