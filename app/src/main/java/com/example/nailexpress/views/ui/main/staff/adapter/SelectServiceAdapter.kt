package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.ILoadMoreAction
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItemSelectServiceBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show
import com.example.nailexpress.models.ui.main.Skill

interface ISelectServiceAdapter : ILoadMoreAction {
    val onItemSelect: ((Skill) -> Unit)
}

class SelectServiceAdapter(val action: ISelectServiceAdapter) :
    PageRecyclerAdapter<Skill, ItemSelectServiceBinding>(action) {

    override fun onBindHolder(
        item: Skill,
        binding: ItemSelectServiceBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            tvName.text = item.name
            tvPrice.show(!item.isService)
            tvPrice.text = item.price_display //NOTE 1

            root.onClick {
                action.onItemSelect.invoke(item)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.item_select_service
}