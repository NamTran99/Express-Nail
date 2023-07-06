package com.example.nailexpress.views.ui.main.profile.adapters

import android.support.core.view.ILoadMoreAction
import android.support.core.view.bindingOf
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemListStaffBinding
import com.example.nailexpress.databinding.ItemProfileOptionBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.models.ui.main.Cv

class ProfileOptionAdapter() :
    SimpleRecyclerAdapter<AccountOption, ItemProfileOptionBinding>() {

    override fun onCreateBinding(parent: ViewGroup): ItemProfileOptionBinding {
        return parent.bindingOf(ItemProfileOptionBinding::inflate)
    }

    override fun onBindHolder(item: AccountOption, binding: ItemProfileOptionBinding, adapterPosition: Int) {
        binding.apply {
            tvTitle.setText(item.title)
            root.onClick{
                item.onItemClick.invoke()
            }
        }
    }
}

data class AccountOption(@StringRes val title: Int, val onItemClick:(()-> Unit))