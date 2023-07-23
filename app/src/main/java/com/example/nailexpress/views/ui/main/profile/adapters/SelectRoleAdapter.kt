package com.example.nailexpress.views.ui.main.profile.adapters

import android.util.Log
import androidx.annotation.StringRes
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.SimpleSelectorRecyclerAdapter
import com.example.nailexpress.databinding.ItemSelectRoleBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show

class SelectRoleAdapter() :
    SimpleSelectorRecyclerAdapter<RoleOption, ItemSelectRoleBinding>() {

    override fun onBindHolder(
        item: RoleOption,
        binding: ItemSelectRoleBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            tvName.setText(item.title)
            imgTick.show(item.isCheck)
            root.onClick {
                Log.d("TAG", "onBindHolder: onclick")
                if (item.isCheck) return@onClick
//                unselectAll()

                submit(
                    mitems.map {
                        it.isCheck = it == item
                        it
                    }
                )
//                refreshData()

//                notifyItemChanged(adapterPosition)
            }
        }
    }

//    fun unselectAll(){
//        mitems.forEach {
//            it.isCheck = false
//        }
//    }

    override val layoutId: Int
        get() = R.layout.item_select_role
}

interface ISelector {
    val isCheck: Boolean
}

data class RoleOption(
    @StringRes val title: Int,
    override var isCheck: Boolean = false,
    val appRole: AppConfig.AppRole
) : ISelector{
    override fun hashCode(): Int {
        return isCheck.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this.isCheck == (other as RoleOption).isCheck
    }
}