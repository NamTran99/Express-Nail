package com.example.nailexpress.views.ui.main.staff.adapter

import com.example.nailexpress.R
import com.example.nailexpress.base.SimpleSelectorRecyclerAdapter
import com.example.nailexpress.databinding.ItemBookSelectServiceBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show
import com.example.nailexpress.models.ui.main.Skill

class BookSelectServiceAdapter() :
    SimpleSelectorRecyclerAdapter<Skill, ItemBookSelectServiceBinding>() {
    override val layoutId: Int
        get() = R.layout.item_book_select_service

    override fun onBindHolder(
        item: Skill,
        binding: ItemBookSelectServiceBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            root.onClick{
                selectMore(item)
            }
            checkbox.isChecked = item.isCheck
            tvName.text = item.name
            tvSalary.text = item.price_display
            tvSalary.show(item.isService)
        }
    }
}