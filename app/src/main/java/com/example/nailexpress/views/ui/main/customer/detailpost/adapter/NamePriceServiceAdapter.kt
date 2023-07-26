package com.example.nailexpress.views.ui.main.customer.detailpost.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nailexpress.databinding.ItemNameAndPriceServiceBinding
import com.example.nailexpress.databinding.ItemNameServiceBinding
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Skill

class NamePriceServiceAdapter(
    private val viewType: ViewTypeService
) : RecyclerView.Adapter<ViewHolder>() {

    private var listData: MutableList<SkillDTO> = mutableListOf()

    fun setData(list: List<SkillDTO>?) {
        listData = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewTypeService.Name.type -> {
                val binding = ItemNameServiceBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NameVH(binding)
            }

            ViewTypeService.NameAndPrice.type -> {
                val binding = ItemNameAndPriceServiceBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NamePriceVH(binding)
            }

            else -> {
                val binding = ItemNameServiceBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NameVH(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = listData[position]
        when (holder.itemViewType) {
            ViewTypeService.Name.type -> {
                if (holder is NameVH) {
                    holder.bindView(itemData)
                }
            }

            ViewTypeService.NameAndPrice.type -> {
                if (holder is NamePriceVH) {
                    holder.bindView(itemData)
                }
            }
        }
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemId(position: Int): Long {
        return viewType.type.toLong()
    }

    inner class NameVH(
        private val binding: ItemNameServiceBinding
    ) : ViewHolder(binding.root) {

        fun bindView(skillDTO: SkillDTO) {
            binding.tvName.text = if (skillDTO.name.isNullOrBlank()) skillDTO.custom_skill else skillDTO.name
        }
    }

    inner class NamePriceVH(
        private val binding: ItemNameAndPriceServiceBinding
    ) : ViewHolder(binding.root) {

        fun bindView(skillDTO: SkillDTO) {
            with(binding) {
                tvName.text = if (skillDTO.name.isNullOrBlank()) skillDTO.custom_skill else skillDTO.name
                tvPrice.text = skillDTO.price.toString()
            }
        }
    }
}

enum class ViewTypeService(val type: Int) {
    Name(0),
    NameAndPrice(1)
}