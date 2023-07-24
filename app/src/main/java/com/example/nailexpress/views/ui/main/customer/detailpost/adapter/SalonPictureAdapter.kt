package com.example.nailexpress.views.ui.main.customer.detailpost.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nailexpress.databinding.ItemSalonPictureBinding
import com.example.nailexpress.extension.setImageURICustom
import com.example.nailexpress.models.ui.AppImage

class SalonPictureAdapter : RecyclerView.Adapter<SalonPictureAdapter.SalonPictureVH>() {

    private var listData: MutableList<AppImage> = mutableListOf()

    fun setData(list: List<AppImage>?) {
        listData = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalonPictureVH {
        val binding = ItemSalonPictureBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SalonPictureVH(binding)
    }

    override fun onBindViewHolder(holder: SalonPictureVH, position: Int) {
        val itemData = listData[position]
        holder.bindView(itemData)
    }

    override fun getItemCount(): Int = listData.size

    inner class SalonPictureVH(
        private val binding: ItemSalonPictureBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(appImage: AppImage) {
            binding.image.setImageURICustom(appImage.image)
        }
    }
}