package com.example.nailexpress.views.ui.main.common.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.nailexpress.R
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemCityFindingBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.models.ui.main.CityDTO

class SelectCityAdapter(view: RecyclerView) :
    SimpleRecyclerAdapter<CityDTO, ItemCityFindingBinding>() {
    init{
        view.adapter = this
    }

    var onClickItem: ((CityDTO) -> Unit) = {}

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindHolder(
        item: CityDTO,
        binding: ItemCityFindingBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            tvCity.text = item.city
            root.onClick {
                onClickItem.invoke(item)
            }
        }
    }

    override val layoutId = R.layout.item_city_finding
}