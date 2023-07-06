package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.bindingOf
import android.view.ViewGroup
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemBookServiceBinding
import com.example.nailexpress.databinding.ItemListServiceBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.models.ui.main.BookServiceForm

interface IBookServiceAdapter{
    val onClickRemoveService: ((BookServiceForm) -> Unit)
    val onVisibleItem: ((Boolean) -> Unit)
}

class BookServiceAdapter(val action: IBookServiceAdapter) :
    SimpleRecyclerAdapter<BookServiceForm, ItemBookServiceBinding>() {

    override fun onCreateBinding(parent: ViewGroup): ItemBookServiceBinding {
        return parent.bindingOf(ItemBookServiceBinding::inflate)
    }

    override fun onBindHolder(item: BookServiceForm, binding: ItemBookServiceBinding, adapterPosition: Int) {
        binding.apply {
            tvName.text = item.skill_name
            tvSalary.text = item.unit_display
            tvDelete.onClick{
                action.onClickRemoveService.invoke(item)
                removeData(item)
            }
        }
    }

    override fun addData(item: BookServiceForm) {
        if(itemCount == 0){
            action.onVisibleItem.invoke(true)
        }
        super.addData(item)

    }

    override fun clear() {
        super.clear()
        action.onVisibleItem.invoke(false)
    }
}