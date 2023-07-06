package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.bindingOf
import android.view.ViewGroup
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemListServiceBinding
import com.example.nailexpress.models.ui.main.ISkill

class DetailServiceAdapter() :
    SimpleRecyclerAdapter<ISkill, ItemListServiceBinding>() {

    override fun onCreateBinding(parent: ViewGroup): ItemListServiceBinding {
        return parent.bindingOf(ItemListServiceBinding::inflate)
    }

    override fun onBindHolder(item: ISkill, binding: ItemListServiceBinding, adapterPosition: Int) {
        binding.apply {
            tvName.text = item.name
            tvSalary.text = item.priceDisplay
        }
    }
}