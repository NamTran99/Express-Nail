package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.bindingOf
import android.util.Log
import android.view.ViewGroup
import com.example.nailexpress.R
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemBookServiceBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show
import com.example.nailexpress.models.ui.main.BookServiceForm

interface IBookServiceAdapter{
    val onClickRemoveService: ((BookServiceForm) -> Unit)
    val onVisibleRecycler: ((Boolean) -> Unit)
}

class BookServiceAdapter(val action: IBookServiceAdapter) :
    SimpleRecyclerAdapter<BookServiceForm, ItemBookServiceBinding>() {

    override fun onBindHolder(item: BookServiceForm, binding: ItemBookServiceBinding, adapterPosition: Int) {
        binding.apply {
            tvName.text = item.skill_name
            tvSalary.show(!item.isTypeTime)
            tvSalary.text = item.price_display
            tvDelete.onClick{
                action.onClickRemoveService.invoke(item)
                removeData(item)
            }
        }
    }

    override fun addData(item: BookServiceForm) {
        action.onVisibleRecycler.invoke(true)
        super.addData(item)
    }

    override fun submit(newItems: List<BookServiceForm>) {
        action.onVisibleRecycler.invoke(newItems.isNotEmpty())
        super.submit(newItems)
    }

    override fun clear() {
        action.onVisibleRecycler.invoke(false)
        super.clear()
    }

    override val layoutId: Int
        get() = R.layout.item_book_service
}