package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.ILoadMoreAction
import android.support.core.view.bindingOf
import android.view.ViewGroup
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemListServiceBinding
import com.example.nailexpress.databinding.ItemSelectServiceBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.models.ui.main.ISkill
import com.example.nailexpress.models.ui.main.Service

interface ISelectServiceAdapter: ILoadMoreAction{
    val onItemSelect: ((Service) -> Unit)
}

class SelectServiceAdapter(val action: ISelectServiceAdapter) :
    PageRecyclerAdapter<Service, ItemSelectServiceBinding>(action) {

    override fun onCreateBinding(parent: ViewGroup): ItemSelectServiceBinding {
        return parent.bindingOf(ItemSelectServiceBinding::inflate)
    }

    override fun onBindHolder(item: Service, binding: ItemSelectServiceBinding, adapterPosition: Int) {
        binding.apply {
            tvName.text = item.name

            root.onClick{
                action.onItemSelect.invoke(item)
            }
        }
    }
}