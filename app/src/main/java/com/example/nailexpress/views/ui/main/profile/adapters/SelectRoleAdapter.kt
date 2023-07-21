package com.example.nailexpress.views.ui.main.profile.adapters

import androidx.annotation.StringRes
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemProfileOptionBinding
import com.example.nailexpress.extension.onClick

class SelectRoleAdapter() :
    SimpleRecyclerAdapter<RoleOption, ItemProfileOptionBinding>() {


    override fun onBindHolder(item: RoleOption, binding: ItemProfileOptionBinding, adapterPosition: Int) {
        binding.apply {
            tvTitle.setText(item.title)
            root.onClick{
                if(item.isCheck) return@onClick
                unselectAll()
                item.isCheck = true
                notifyItemChanged(adapterPosition)
            }
        }
    }

    fun unselectAll(){
        mitems.forEach {
            it.isCheck = false
        }
        refreshData()
    }

    override val layoutId: Int
        get() = R.layout.item_profile_option
}

data class RoleOption(@StringRes val title: Int, var isCheck: Boolean = false, val appRole: AppConfig.AppRole)