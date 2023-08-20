package com.example.nailexpress.views.ui.main.staff.adapter

import com.example.nailexpress.R
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemListServiceBinding
import com.example.nailexpress.extension.show
import com.example.nailexpress.models.ui.main.Skill

class DetailServiceAdapter() :
    SimpleRecyclerAdapter<Skill, ItemListServiceBinding>() {

    override fun onBindHolder(item: Skill, binding: ItemListServiceBinding, adapterPosition: Int) {
        binding.apply {
            tvName.text = item.name
            tvSalary.text = item.price_display
            tvSalary.show(item.isService)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_list_service
}