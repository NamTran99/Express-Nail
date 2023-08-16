package com.example.nailexpress.views.ui.main.customer.detailpost.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nailexpress.app.SalaryType
import com.example.nailexpress.databinding.ItemNameAndPriceServiceBinding
import com.example.nailexpress.databinding.ItemNameServiceBinding
import com.example.nailexpress.extension.formatPrice
import com.example.nailexpress.models.response.SkillDTO

class NamePriceServiceAdapter(
    private val viewType: SalaryType
) : RecyclerView.Adapter<ViewHolder>() {

    private var listData: MutableList<SkillDTO> = mutableListOf()

    fun setData(list: List<SkillDTO>?) {
        listData = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SalaryType.Time.data -> {
                val binding = ItemNameServiceBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NameVH(binding)
            }

            else -> {
                val binding = ItemNameAndPriceServiceBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NamePriceVH(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = listData[position]
        when (holder.itemViewType) {
            SalaryType.Time.data -> {
                if (holder is NameVH) {
                    holder.bindView(itemData)
                }
            }

            SalaryType.Service.data -> {
                if (holder is NamePriceVH) {
                    holder.bindView(itemData)
                }
            }
        }
    }

    override fun getItemCount(): Int = listData.size

//    override fun getItemId(position: Int): Long {
//        return viewType.data.toLong()
//    }

    override fun getItemViewType(position: Int): Int {
        return viewType.data
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
                tvPrice.text = skillDTO.price?.formatPrice()
            }
        }
    }
}

//enum class ViewTypeService(val type: Int) {
//    Name(0),
//    NameAndPrice(1)
//}